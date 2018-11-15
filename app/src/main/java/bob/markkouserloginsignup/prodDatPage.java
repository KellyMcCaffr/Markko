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

    String totalLikes;
    String totalDislikes;
    int currLabelCount;
    int xLabelNum=0;

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
        //Set chart for likes vs dislikes:
        ArrayList<String> entryValues=new ArrayList<String>();
        entryValues.add(totalLikes);
        entryValues.add(totalDislikes);

        final ArrayList<String> labelValues=new ArrayList<String>();
        labelValues.add("Likes");
        labelValues.add("Dislikes");

        int[] mTemplate=ColorTemplate.MATERIAL_COLORS;
        setChart(likeInfoChart, entryValues, labelValues, mTemplate);
        //We must reset the label count after each call(needs to be global?)
        currLabelCount=0;
        /**
        setEthnicityView();
        setReligionView();
        setAgeView();
        setEducationView();
        setGenderView();**/
    }


    private void setChart(BarChart mChart, ArrayList<String> entryValues,final ArrayList<String> labelValues
      , int[] mTemplate){
        //ArrayList<String> entryValues=new ArrayList<String>();
        //ArrayList<String> labelValues=new ArrayList<String>();

        //The number of actual labels+1;
        //int labelCount=3;
        final int labelCount=labelValues.size();
        Log.d("Label size:",""+labelValues.size());
        //int maxGraphVal=10;
        int maxGraphVal=40;

        float i=maxGraphVal/(entryValues.size());
        //int entrySpaceInterval=5;
        float entrySpaceInterval=i;
        float barWidth=i;

        //Create counts for iterating through the arrays provided in parameters:
        currLabelCount=0;
        int currEntryCount=0;

        //We assume that these are ints as strings:
        entryValues.add(totalLikes);
        entryValues.add(totalDislikes);

        ArrayList<BarEntry> entries = new ArrayList<>();

        while(currEntryCount<entryValues.size()) {
            if (entryValues.get(currEntryCount).length() > 0) {
                entries.add(new BarEntry(i, Integer.parseInt(entryValues.get(0))));
            } else {
                entries.add(new BarEntry(i, 0));
            }
            currEntryCount++;
            i += entrySpaceInterval;
        }
        dataSet = new BarDataSet(entries, "1");
        BarData data = new BarData(dataSet);

        dataSet.setValueTextSize(0);

        dataSet.setColors(mTemplate);
        mChart.setData(data);
        mChart.setScaleEnabled(false);

        //Sets the width of each bar:
        mChart.getBarData().setBarWidth(5);

        mChart.setTouchEnabled(false);
        //Set and format the description:
        mChart.setDescription("");
        //likeInfoChart.setDescriptionTextSize(35);
        //likeInfoChart.setDescriptionPosition(600,14);
        final XAxis axisX=mChart.getXAxis();
        axisX.setAxisMaxValue(currEntryCount);
        axisX.setDrawLabels(true);
        axisX.setTextSize(16);
        axisX.setGranularity(5);
        mChart.getBarData().setBarWidth(barWidth);
        //Count must be 1 greater than the actual number of labels:
        axisX.setLabelCount(labelCount+1,true);
        axisX.setAxisMaxValue(maxGraphVal);
        //Set the x-axis labels:
        axisX.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String mValue="";
                if(currLabelCount>=(labelCount)){
                    currLabelCount=0;
                }

                mValue=labelValues.get(currLabelCount);
                currLabelCount++;

                Log.d("Label: ", ""+mValue);
                return mValue;

            }
            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

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

    }


    private void setEthnicityView(){
        String ethnicityString=busProdDB.getEthnicityString(enteredUsername,productName);
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
        String ethnFinalString="Black: "+blackLikes+" White: "+whiteLikes+" Asian: "+asianLikes+" Other: "+otherLikes;

    }
    private void setReligionView(){
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
        String religionFinalString="Christian: "+christianLikes+" Muslim: "+muslimLikes+" Buddhist: "+buddhistLikes
                +" Jewish: "+jewishLikes+" Atheist: "+atheistLikes+" Other: "+otherLikes;

    }
    private void setAgeView(){
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
        String religionFinalString="<18: "+a1Likes+" 18-25: "+a2Likes+" 25-35: "+a3Likes
                +" 35+: "+a4Likes;

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
