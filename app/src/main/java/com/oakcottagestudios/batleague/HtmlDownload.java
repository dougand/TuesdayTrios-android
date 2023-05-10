package com.oakcottagestudios.batleague;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Doug on 18/02/2015.
 */
public class HtmlDownload implements Globals {
	protected Vector<Vector<Vector<String>>> tableList;

	private Vector<Vector<String>> currentTable;
	private Vector<String> currentRow;
	private int itemCount;

	private boolean inTable;
	private boolean inTableData;

	private StringBuffer itemString;

	protected int downloadState = STATE_IDLE;
	protected String downloadStateText = "";
	protected int downloadStateCol = 0x0;

	protected int tableType;
	protected String LogID;
	protected String Url;
	protected int expectedSize;

	public static int totalFiles = 0;
	public static int totalExpectedSize = 0;
	public static int totalDownloaded = 0;


	protected DownloadListener listener = null;


	public void init( int type, String log, int size)
	{
		tableType=type;
		LogID=log;
		expectedSize=size*1024;

		totalFiles++;
		totalExpectedSize += expectedSize;
	}


	public void setDownloadState( int state )
	{

		downloadState = state;

		switch( downloadState )
		{
			case STATE_CONNECTING:
				downloadStateText = "Connecting...";
				downloadStateCol = 0xFF000000;
				break;

			case STATE_CONNECTION_ERROR:
				downloadStateText = "Connection Error";
				downloadStateCol = 0xFFFF0000;
				break;

			case STATE_DOWNLOAD_ERROR:
				downloadStateText = "Download Error";
				downloadStateCol = 0xFFFF0000;
				break;

			case STATE_DATA_ERROR:
				downloadStateText = "Error Analysing Data";
				downloadStateCol = 0xFFFF0000;
				break;

			case STATE_CANCELLED:
				downloadStateText = "Cancelled";
				downloadStateCol = 0xFFFF0000;
				break;


			case STATE_PARSING:
				downloadStateText = "Parsing...";
				downloadStateCol = 0xFF000000;
				break;

			case STATE_PARSE_ERROR:
				downloadStateText = "Error parsing data";
				downloadStateCol = 0xFFFF0000;
				break;


			case STATE_FINISHED:
			default:
				downloadStateText = "";
				break;


		}
	}



	public class FileDownloader extends AsyncTask<String, Integer, ByteArrayOutputStream>
	{
		protected ByteArrayOutputStream doInBackground(String... sUrl)
		{
			InputStream input = null;
			ByteArrayOutputStream output = null;
			HttpURLConnection connection = null;
			try
			{
				publishProgress(STATE_CONNECTING);
				URL url = new URL(sUrl[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				if( connection.getResponseCode() != HttpURLConnection.HTTP_OK )
				{
					publishProgress(STATE_CONNECTION_ERROR);
					return null;
				}

				int expected = connection.getContentLength();
				Log.d(LogID, "Expected: "+expected);
				//Thread.sleep( 500 );
				// download the file
				input = connection.getInputStream();
				output = new ByteArrayOutputStream();

				byte data[] = new byte[4096];
				int count;
				int total = 0;
				while( (count = input.read(data)) != -1 )
				{
					// allow canceling with back button
					if( isCancelled() )
					{
						input.close();
						publishProgress(STATE_CANCELLED);
						return null;
					}
					total += count;
					totalDownloaded += count;
					output.write(data, 0, count);
					Log.d(LogID, "Downloaded: " + count + "/" + total);
					int progress = (total*100)/expectedSize;
					if( progress>100 ) progress = 100;
					publishProgress( STATE_DOWNLOAD_PROGRESS + progress );
				}
				Log.d(LogID, "Downloaded: " + total);
			}
			catch( Exception e )
			{
				Log.d(LogID, "Download error: " + e);
				publishProgress(STATE_DOWNLOAD_ERROR);
				return null;
			}
			finally
			{
				try
				{
					if( output != null )
						output.close();
					if( input != null )
						input.close();
				}
				catch( IOException ignored )
				{
				}

				if( connection != null )
					connection.disconnect();
			}

			return output;
		}

		protected void onProgressUpdate(Integer... progress)
		{
			setDownloadState(progress[0]);
		}


		protected void onPostExecute(ByteArrayOutputStream result)
		{
			try
			{

				if( result != null )
				{
					String htmlStr = result.toString( "windows-1252" );
					setDownloadState(STATE_PARSING);

					//Thread.sleep(500);

					try
					{
						initHtmlTables();
						parseHtml(htmlStr);

					}
					catch( Exception e )
					{
						e.printStackTrace(System.out);
						setDownloadState(STATE_PARSE_ERROR);
						return;
					}

					debugTableContent();
					//Thread.sleep(500);
					setDownloadState(STATE_FINISHED);
				}
			}
			catch (Exception e){}

		}

	}



	protected void parseHtml( String htmlStr ) throws Exception
	{
		boolean inTag = false;
		boolean isEndTag = false;
		boolean skipAttribs = false;

		StringBuffer tagBuffer = new StringBuffer(32);
		StringBuffer contentBuffer = new StringBuffer(256);

		for( int i=0; i<htmlStr.length(); i++ )
		{
			char c = htmlStr.charAt(i);

			if( inTag )
			{
				if( c == '>' || c=='<' )
				{
					//  Found the End of the Tag
					parserHandleTag(tagBuffer.toString().toLowerCase(), !isEndTag);
					tagBuffer.setLength(0);
					inTag = false;
					isEndTag = false;
					skipAttribs = false;

					if( c=='<' ) i--;       //  Fix for html bug where no end '>'
				}
				else if( !skipAttribs )
				{
					if( c == '/' )
						isEndTag = true;
					else if( Character.isWhitespace(c) )
						skipAttribs = true;
					else
						tagBuffer.append(c);
				}
			}
			else
			{
				if( c == '<' )
				{
					inTag = true;
					String contentStr = contentBuffer.toString().trim();
					if( contentStr.length() > 0 )
						parserHandleContent(contentStr);

					contentBuffer.setLength(0);
				}
				else
					contentBuffer.append(c);

			}

		}
	}



	protected void initHtmlTables()
	{
		currentRow = new Vector();
		currentTable = new Vector();
		tableList = new Vector();

		itemString = new StringBuffer( 256 );
		itemCount = 0;

		inTable = false;
		inTableData = false;
	}



	protected void parserHandleTag( String tagStr, boolean isStartTag )
	{
		if( tagStr.equals("table") )
		{
			if( isStartTag )
			{
				//  Start of <table>
				//Log.v("TABLE", "<table>");
				if( inTable )
				{
					Log.e( LogID, "Error: Nested Tables in HTML!!");
				}

				inTable = true;
				currentTable = new Vector();
				currentRow = new Vector();
			}
			else
			{
				//Log.v("TABLE", "</table>");
				//Log.v( "TABLE", "--------------------------------------------------------" );
				if( !inTable )
				{
					Log.e( LogID, "Error: End of table found before start!!" );
				}

				if( itemCount>0 )
				{
					currentTable.add( currentRow );
					//Log.v("TABLE", "Row added to table. Count=" + itemCount);
				}

				inTable = false;
				if( !currentTable.isEmpty() )
				{
					tableList.add( currentTable );
				}
			}
		}
		else if( tagStr.equals("tr") )
		{
			//Log.v( "TABLE", "<tr>" );
			if( itemCount>0 )
			{
				currentTable.add( currentRow );
				//Log.v("TABLE", "Row added to table. Count=" + itemCount);
				currentRow = new Vector();
				itemCount = 0;
			}

		}
		else if( tagStr.equals("td") )
		{
			inTableData = isStartTag;
			if( inTableData )
			{
				//Log.v("TABLE", "<td>");
				itemString.setLength(0);
			}
			else
			{
				//Log.v( "TABLE", "</td>" );
				currentRow.add( itemString.toString() );
				itemCount++;
			}
		}

	}


	protected void parserHandleContent( String contentStr )
	{
		//Log.v( "TABLE", "characters : "+contentStr );
		if( inTableData )
			itemString.append(contentStr);
	}


	protected void debugTableContent()
	{
		int tableCount = tableList.size();

		Log.d( LogID, "No of tables: "+tableCount );
		Log.d( LogID, "----------------" );

		for( int t=0; t<tableCount; t++ )
		{
			Vector<Vector<String>> table = tableList.elementAt(t);
			int rowCount = table.size();
			Log.d( LogID, "No of Rows: " + rowCount );

			for( int r=0; r<rowCount; r++ )
			{
				Vector<String> row = table.elementAt(r);
				StringBuffer strbuf = new StringBuffer();

				int items = row.size();

				for( int d=0; d<items; d++ )
				{
					if( d>0 ) strbuf.append(" | ");
					strbuf.append( row.elementAt(d) );
				}
				Log.d( LogID, "Row "+r+": (" + items + ") " +strbuf.toString() );
			}

			Log.d( LogID, "----------------" );
		}

	}


}
