package com.dbcloner.common;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PreviousConnectionsManager {
    public static List<PreviousConnection> getPreviousConnections() throws Exception{
        List<PreviousConnection> list=new ArrayList<PreviousConnection>();
        BaseController baseController = new BaseController();
        String s=baseController.readFileToString(new File(System.getProperty("user.dir")+"/preference/connections.json"));
        JSONObject json = new JSONObject(s);
        JSONArray array = json.getJSONArray("connections");
        for(int i=0 ; i<array.length() ; i++){
            JSONObject obj = array.getJSONObject(i);
            Gson gson = new GsonBuilder().create();
            PreviousConnection conn = gson.fromJson(obj.toString(),PreviousConnection.class);
            list.add(conn);
        }
        return list;
    }
    public static void saveConnection(String url,String username,String password) throws Exception{
        String s;
        BaseController baseController = new BaseController();
        File f=new File(System.getProperty("user.dir") + "/preference");
        if(f.exists()) {
            s = baseController.readFileToString(f.listFiles()[0]);
            JSONObject json = new JSONObject(s);
            JSONArray array = json.getJSONArray("connections");
            for(int i=0 ; i<array.length() ; i++){
                JSONObject obj = array.getJSONObject(i);
                if(obj.getString("username").equals(username) && obj.getString("url").equals(url)){
                    return;
                }
            }
            JSONObject newObject = new JSONObject();
            newObject.put("url",url);
            newObject.put("username",username);
            newObject.put("password",password);
            json.getJSONArray("connections").put(newObject);
            FileOutputStream writer = new FileOutputStream(System.getProperty("user.dir")+"/preference/connections.json");
            try {
                writer.write(("").getBytes());
                writer.write(json.toString().getBytes());
                writer.close();
            }catch(Exception e){
                writer.close();
                throw e;
            }
        }else{
            f.mkdirs();
            JSONObject json = new JSONObject();
            JSONObject newObject = new JSONObject();
            newObject.put("url",url);
            newObject.put("username",username);
            newObject.put("password",password);
            JSONArray array = new JSONArray();
            array.put(newObject);
            json.put("connections",array);
            FileOutputStream writer = new FileOutputStream(System.getProperty("user.dir")+"/preference/connections.json");
            try {
                writer.write(("").getBytes());
                writer.write(json.toString().getBytes());
                writer.close();
            }catch(Exception e){
                writer.close();
                throw e;
            }
        }
    }
}
