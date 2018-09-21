package com.schoolstuff.adrian.koshertracker;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DataHandleTask extends AsyncTask <Void,Void,List<DataIndexer>> {

    private Context context;
    private XmlPullParser parser;
    private InputStream inputStream;

    public DataHandleTask(Context context){
        this.context=context;
        try {
            this.inputStream = init_inputStream();
        }
        catch(Exception e){
            Log.i("#Exception",e.getMessage());
        }
        init_XMLParser();
    }

    @Override
    protected List<DataIndexer> doInBackground(Void... params) {
        Log.i("#Prograss", "doing in background");
        try {

            List<DataIndexer> mData = readData();
            Log.i("#TheData", mData.toString());
            inputStream.close();
            return mData;
        } catch (Exception e) {

            Log.i("#Exception", e.getMessage());

        }
        return null;
    }
    //DONE insert the array list into the databse

    @Override
    public void onPostExecute(List<DataIndexer> result){
        Log.i("#PostExecute","Databse Insertion Initialized");
        Log.i("#Data size",Integer.toString(result.size()));
        int i=0;
        DBTools db_tools=new DBTools(context);
        for (DataIndexer data:result) {
            boolean bool =db_tools.insert_data(data);
            Log.i("#Insertion Status",""+i+ (bool ? " succcess":" failure"));
            i++;
        }
        ((MainActivity) context).runOnUiThread(new UIMessage());
    }

    //Gets a Reources object reference, and opens a raw resource from the raw resources file, and gets the inputstream of mdata
    private InputStream init_inputStream() throws IOException {
        return context.getAssets().open("mdata.xml");
    }

    private void init_XMLParser() {
        try {
            this.parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(this.inputStream, null);
        }
        catch (Exception e){
            Log.i("@XMLException",e.getMessage());
        }
    }
    public List<DataIndexer> readData() throws XmlPullParserException, IOException {
        List<DataIndexer> data=new ArrayList<>();
        int eventType=parser.getEventType();
        DataIndexer restaurant =null;
        while (eventType!=XmlPullParser.END_DOCUMENT){
            String name=null;

            if (eventType == XmlPullParser.START_TAG){

                name=parser.getName();
                if (name.equals("Restaurant")) {
                    restaurant = new DataIndexer();
                    data.add(restaurant);
                }
                else if (name.equals("name"))
                    restaurant.name=parser.nextText();
                else if(name.equals("city"))
                    restaurant.city=parser.nextText();
                else if(name.equals("address"))
                    restaurant.address=parser.nextText();
                else if(name.equals("type"))
                    restaurant.type=parser.nextText();
                else if(name.equals("cert"))
                    restaurant.cert=Integer.parseInt(parser.nextText());
            }
            eventType=parser.next();
        }
        return data;
    }

public class UIMessage implements Runnable{
        public void run(){
            DBTools tool=new DBTools(context);
            //TextView tv=((MainActivity) context).findViewById(R.id.textView);
            //tv.setText(tool.fetch_data(17).toString());
        }

}
}
