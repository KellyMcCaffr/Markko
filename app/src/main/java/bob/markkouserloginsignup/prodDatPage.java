package bob.markkouserloginsignup;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import android.util.Log;

import java.util.ArrayList;

public class prodDatPage extends Activity {
    int formatValueTime=1;
    int inc=0;

    BarChart likeInfoChart;
    BarChart ethnicityChart;
    BarChart religionChart;
    BarChart ageChart;
    BarChart educationChart;
    BarChart genderChart;

    BarDataSet dataSet;

    String enteredUsername;
    String planName;
    String productNumber;
    String productName;

    ArrayList<String> mLabelVals;
    String totalLikes;
    String totalDislikes;
    int currLabelCount;
    int i;


    busProductsInformationDatabaseManager busProdDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_dat_page);
        likeInfoChart =findViewById(R.id.likeInfoChart);
        ethnicityChart =findViewById(R.id.ethnicityChart);
        religionChart =findViewById(R.id.religionChart);
        ageChart =findViewById(R.id.ageChart);
        educationChart =findViewById(R.id.educationChart);
        genderChart =findViewById(R.id.genderChart);

        busProdDB=new busProductsInformationDatabaseManager(this,null,null,1);



        Bundle paramsPassed;
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        planName = paramsPassed.getString("planName");
        productNumber = paramsPassed.getString("productNumber");
        productName=busProdDB.getProductNameByIdentifier(enteredUsername,productNumber);
        totalLikes=busProdDB.getTotalLikes(enteredUsername,productName);
        totalDislikes=busProdDB.getTotalDislikes(enteredUsername,productName);
        //We must reset the label count after each call(needs to be global?)
        currLabelCount=0;
        formatValueTime=1;
        setLikesChart();
        currLabelCount=0;
        formatValueTime=1;
        setEthnicityChart();
        currLabelCount=0;
        formatValueTime=1;
        setReligionChart();
        currLabelCount=0;
        formatValueTime=0;
        setAgeChart();
        //setEducationChart();
        //setGenderChart();
    }
    //A generalized function for setting MPAndroid bar charts with varying templates, id and entries:
    private void setChart(BarChart mChart, ArrayList<String> entryValues,final ArrayList<String> labelValues
      , int[] mTemplate){

        //The number of actual labels+1(I don't know why we must add 1);
        final int labelCount=labelValues.size();

        //The center x-coordinate for the first value:
        i=entryValues.size()/2;
        //The max x-value on the graph:
        int maxGraphVal=entryValues.size()*entryValues.size();
        Log.d("Max:", ""+maxGraphVal);
        //The min x-value on the graph:
        int minGraphVal=0;
        //The x-width of each bar:
        int mWidth=entryValues.size();
        int mInt;


        //Create counts for iterating through the arrays provided in parameters:
        currLabelCount=0;
        int currEntryCount=0;


        ArrayList<BarEntry> entries = new ArrayList<>();

        while(currEntryCount<entryValues.size()) {
            if (entryValues.get(currEntryCount).length() > 0) {
                Log.d("I added:",""+i);
                mInt=Integer.parseInt(entryValues.get(currEntryCount));
                entries.add(new BarEntry(i,mInt));
            } else {
                entries.add(new BarEntry(i, 0));
            }
            currEntryCount++;
            //i += entrySpaceInterval;
            i+=mWidth;
        }
        dataSet = new BarDataSet(entries, "1");
        BarData data = new BarData(dataSet);

        dataSet.setValueTextSize(0);

        dataSet.setColors(mTemplate);
        mChart.setData(data);
        mChart.setScaleEnabled(false);

        //Sets the width of each bar:
        mChart.getBarData().setBarWidth(mWidth);

        mChart.setTouchEnabled(false);
        //Set and format the description:
        mChart.setDescription("");
        final XAxis axisX=mChart.getXAxis();
        axisX.setDrawLabels(true);
        axisX.setTextSize(12);
        //Count must be 1 greater than the actual number of labels:
        axisX.setLabelCount(labelCount+1,true);
        axisX.setAxisMaxValue(maxGraphVal);
        axisX.setAxisMinValue(minGraphVal);
        axisX.setPosition(XAxis.XAxisPosition.BOTTOM);
        axisX.setCenterAxisLabels(true);

        YAxis axisRight=mChart.getAxisRight();

        axisRight.setEnabled(false);

        YAxis axisLeft=mChart.getAxisLeft();

        axisLeft.setGranularity(1);
        axisLeft.setTextSize(18);
        axisLeft.setTextColor(Color.BLACK);

        //Erase the gridlines from the chart:
        axisX.setDrawGridLines(false);
        mChart.getAxisLeft().setDrawGridLines(false);
        axisX.setLabelCount(labelCount+1,true);
        //Set the x-axis labels:
        axisX.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                /**String mValue="";
                if (currLabelCount >= (labelCount)) {
                    currLabelCount = 0;
                }
                Log.d("Here is value:", ""+value);
                mValue = labelValues.get(currLabelCount);
                currLabelCount++;**/
                //return mValue;
                //value is the current value of x:
                int newVal=(int)value;
                Log.d("VAL:", ""+value);
                if(value!=0 && value!=(labelValues.size()*labelValues.size())) {
                    return labelValues.get((newVal / labelValues.size()) - 1+inc);
                }

                else if(value==0){
                    inc=1;
                    return labelValues.get((newVal / labelValues.size()) - 1+inc);
                }
                else{
                    inc=0;
                    return labelValues.get((newVal / labelValues.size()) - 1+inc);
                }


            }
            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

    }

    //Executes the steps required to set up the likes/dislikes bar chart:
    private void setLikesChart(){
        //Set chart for likes vs dislikes:
        ArrayList<String> entryValues=new ArrayList<String>();
        entryValues.add(totalLikes);
        entryValues.add(totalDislikes);

        final ArrayList<String> labelValues=new ArrayList<String>();
        labelValues.add("Likes");
        labelValues.add("Dislikes");

        int[] mTemplate=ColorTemplate.MATERIAL_COLORS;
        setChart(likeInfoChart, entryValues, labelValues, mTemplate);
    }
    private void setEthnicityChart(){
        String ethnicityString=busProdDB.getEthnicityString(enteredUsername,productName);
        Log.d("Ethnicity String: ", ethnicityString);
        String[] sepEthnicities=ethnicityString.split("%");
        int blackLikes=0;
        int whiteLikes=0;
        int asianLikes=0;
        int otherLikes=0;

        String blackString="black";
        String whiteString="white";
        String asianString="asian";
        String otherString="other";

        for(String race:sepEthnicities){
            if(race.equals(blackString)){
                blackLikes+=1;
            }
            else if(race.equals(whiteString)){
                whiteLikes+=1;
            }
            else if(race.equals(asianString)){
                asianLikes+=1;
            }
            else if(race.equals(otherString)){
                otherLikes+=1;
            }
        }
        ArrayList<String> entryValues=new ArrayList<String>();
        entryValues.add(Integer.toString(blackLikes));
        entryValues.add(Integer.toString(whiteLikes));
        entryValues.add(Integer.toString(asianLikes));
        entryValues.add(Integer.toString(otherLikes));



        final ArrayList<String> labelValues=new ArrayList<String>();
        labelValues.add("Black");
        labelValues.add("White");
        labelValues.add("Asian");
        labelValues.add("Other");

        int[] mTemplate=ColorTemplate.MATERIAL_COLORS;
        setChart(ethnicityChart, entryValues, labelValues, mTemplate);

    }
    private void setReligionChart(){
        String religionString=busProdDB.getReligionString(enteredUsername,productName);
        String[] sepReligions=religionString.split("%");
        int christianLikes=0;
        int muslimLikes=0;
        int buddhistLikes=0;
        int jewishLikes=0;
        int atheistLikes=0;
        int otherLikes=0;

        String christianString="Christian";
        String muslimString="Muslim";
        String buddhistString="Buddhist";
        String jewishString="Jewish";
        String atheistString="Atheist";
        String otherString="other";

        for(String race:sepReligions){
            if(race.equals(christianString)){
                christianLikes+=1;
            }
            else if(race.equals(muslimString)){
                muslimLikes+=1;
            }
            else if(race.equals(buddhistString)){
                buddhistLikes+=1;
            }
            else if(race.equals(jewishString)){
                jewishLikes+=1;
            }
            else if(race.equals(atheistString)){
                atheistLikes+=1;
            }
            else if(race.equals(otherString)){
                otherLikes+=1;
            }
        }
        ArrayList<String> entryValues=new ArrayList<String>();
        entryValues.add(Integer.toString(christianLikes));
        entryValues.add(Integer.toString(muslimLikes));
        entryValues.add(Integer.toString(buddhistLikes));
        entryValues.add(Integer.toString(jewishLikes));
        entryValues.add(Integer.toString(atheistLikes));
        entryValues.add(Integer.toString(otherLikes));

        final ArrayList<String> labelValues=new ArrayList<String>();
        labelValues.add("Christian");
        labelValues.add("Muslim");
        labelValues.add("Buddhist");
        labelValues.add("Jewish");
        labelValues.add("Atheist");
        labelValues.add("Other");

        int[] mTemplate=ColorTemplate.MATERIAL_COLORS;
        setChart(religionChart, entryValues, labelValues, mTemplate);

    }
    private void setAgeChart(){
        String ageString=busProdDB.getAgeString(enteredUsername,productName);
        String[] sepAges=ageString.split("%");
        int a1Likes=0;
        int a2Likes=0;
        int a3Likes=0;
        int a4Likes=0;

        for(String age:sepAges){
            if(age.equals("<18")){
                a1Likes+=1;
            }
            else if(age.equals("18-25")){
                a2Likes+=1;
            }
            else if(age.equals("25-35")){
                a3Likes+=1;
            }
            else if(age.equals("35+")){
                a4Likes+=1;
            }

        }
        ArrayList<String> entryValues=new ArrayList<String>();
        entryValues.add(Integer.toString(a1Likes));
        entryValues.add(Integer.toString(a2Likes));
        entryValues.add(Integer.toString(a3Likes));
        entryValues.add(Integer.toString(a4Likes));

        final ArrayList<String> labelValues=new ArrayList<String>();
        labelValues.add("<18");
        labelValues.add("18-25");
        labelValues.add("25-35");
        labelValues.add("35+");
        Log.d("Label values:", ""+labelValues);
        Log.d("Entry values:", ""+entryValues);

        int[] mTemplate=ColorTemplate.MATERIAL_COLORS;
        setChart(ageChart, entryValues, labelValues, mTemplate);

    }

    private void setEducationView(){
        String educationString=busProdDB.getEducationString(enteredUsername,productName);
        String[] sepEducation=educationString.split("%");
        int ed1=0;
        int ed2=0;
        int ed3=0;
        int ed4=0;
        int ed5=0;

        for(String educ:sepEducation){
            if(educ.equals("elementary")){
                ed1+=1;
            }
            else if(educ.equals("middle")){
                ed2+=1;
            }
            else if(educ.equals("high school")){
                ed3+=1;
            }
            else if(educ.equals("college")){
                ed4+=1;
            }
            else if(educ.equals("post-grad")){
                ed5+=1;
            }

        }
        String educationFinalString="elementary: "+ed1+" middle: "+ed2+" high school: "+ed3
                +" college: "+ed4 +" post-grad: "+ed5;

    }
    private void setGenderView(){
        String genderString=busProdDB.getGenderString(enteredUsername,productName);
        String[] sepGender=genderString.split("%");
        int gen1=0;
        int gen2=0;
        int gen3=0;

        for(String gend:sepGender){
            if(gend.equals("male")){
                gen1+=1;
            }
            else if(gend.equals("female")){
                gen2+=1;
            }
            else if(gend.equals("other")){
                gen3+=1;
            }

        }
        String educationFinalString="male: "+gen1+" female: "+gen2+" other: "+gen3;

    }
}
