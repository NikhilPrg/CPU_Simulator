package com.kurama.nikhil.cpusimulator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Nikhil on 19/04/17.
 */
public class ProcessRecordDataSupply {

    SharedPreferences presets;
    SharedPreferences.Editor presetsEditor;
    ArrayList<Process> processList;

    public ProcessRecordDataSupply(Context context) {
        presets = context.getSharedPreferences("presets", Context.MODE_PRIVATE);
        processList = new ArrayList<Process>();
        presetsEditor = presets.edit();
    }

    public void setToPresetList(ArrayList<Process> list, Integer pId){
        Process temp;
        Log.v("inputList", list.toString());
        String input = "";
        for (int i = 0; i < list.size(); i++) {
            temp = list.get(i);
            input+=(temp.getProcessName() + "," + temp.getBurstTime() + "," + temp.getArrivalTime() + ";");
        }
        presetsEditor.putString("P"+pId, input);
        presetsEditor.commit();
        Log.v("added", input +"test1");
    }

    public ArrayList<Process> getPreset(int pId) {
        ArrayList<Process> processes = new ArrayList<Process>();
        String id = "P"+pId;
        String data = presets.getString(id , "empty");
        Log.v("gotData", data +"test");
        if(data.equals("empty")) {
            return processes;
        }
        StringTokenizer tkn = new StringTokenizer(data, ";");
        StringTokenizer tkn2;
        Process temp = new Process("P0", 0 ,0 );
        while(tkn.hasMoreTokens()){
            tkn2 = new StringTokenizer(tkn.nextToken() , ",");
            temp.setProcessName(tkn2.nextToken());
            temp.setBurstTime(Integer.parseInt(tkn2.nextToken().trim()));
            temp.setArrivalTime(Integer.parseInt(tkn2.nextToken().trim()));
            processes.add(temp);
        }
        return processes;
    }

    public String[] getListOfIds(){
        Set<String> myset = presets.getAll().keySet();
        String[] temp = myset.toArray(new String[myset.size()]);
        return temp;
    }
}
