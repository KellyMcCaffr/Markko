package bob.markkouserloginsignup;

import android.app.Activity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

public class busDataPieChart extends Activity {
    String enteredUsername;
    PieChart chart;
    busProductsInformationDatabaseManager busProdInfo;
    ArrayList<String> productLikesPercentData;
    ArrayList<String> existingProductNumbers;

    ArrayList<String> currProductNames;
    PieDataSet pieDataSet ;
    PieData pieData ;
    int numProd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_data_pie_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle paramsPassed;
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        chart= (PieChart) findViewById(R.id.pie_chart);
        busProdInfo=new busProductsInformationDatabaseManager(this,null,null,1);
        numProd=busProdInfo.getAllBusPNames(enteredUsername).size();
        currProductNames=new ArrayList<String>();
        getProductNames();
        getLikesPercents();
        buildPieChart();

    }

    //Used to manually get all product names for the business in sorted order:
    private void getProductNames(){
        String currPName;
        for(int i=0;i<numProd;i++){
            currPName=busProdInfo.getProductNameByIdentifier(enteredUsername,Integer.toString(i+1));
            currProductNames.add(currPName);
        }
    }
    public void buildPieChart(){
        PieChart pieChart = (PieChart) findViewById(R.id.pie_chart);

        List<PieEntry> entries = new ArrayList<PieEntry>();
        int i=0;
        Float percentInt;
        for(String percentLikes: productLikesPercentData) {
            Log.d("BUSDATAPIECHART", "Pname: "+currProductNames.get(i));
            percentInt=Float.parseFloat(percentLikes);
            entries.add(new PieEntry(percentInt,currProductNames.get(i)));
            i+=1;
        }


        List<String> PieEntryLabels =new ArrayList<String>();

        pieDataSet = new PieDataSet(entries, "");

        pieData = new PieData(pieDataSet);

        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        pieChart.setData(pieData);

        pieData.setValueTextSize(16);

        //pieChart.animateY(3000);

        pieChart.setDescriptionTextSize(40);
        pieChart.setDescription("Total likes percent per product");


    }

    private void getLikesPercents(){
        List<String> totalLikes=getTotalLikes();
        productLikesPercentData=new ArrayList<String>();
        float sumTotalLikes=getNumLikes(totalLikes);
        float currLikes;
        float currLikesPercent;
        for(String prodLikes:totalLikes){
            if(prodLikes.length()>0){
                Log.d("currLikes in glP:", prodLikes);
                currLikes= parseInt(prodLikes);
                Log.d("Sum total likes:", Float.toString(sumTotalLikes));
                currLikesPercent=100*(currLikes/sumTotalLikes);
                Log.d("cL percent in glP:", Float.toString(currLikesPercent));
                productLikesPercentData.add(Float.toString(currLikesPercent));
            }
        }
        for(String percent:productLikesPercentData){
            Log.d("Here is percent:", percent);
        }
        Log.d("Final likes p data sze:",Integer.toString(productLikesPercentData.size()));
    }
    private ArrayList getTotalLikes(){
        List<String> pList= Arrays.asList("1","2","3","4","5","6");
        ArrayList<String> totalLikesPerProduct=new ArrayList<String>();
        existingProductNumbers=new ArrayList<String>();
        for(String pN:pList){
            String possibleLikes;
            possibleLikes=busProdInfo.getTotalLikes(enteredUsername,busProdInfo.getProductNameByIdentifier(enteredUsername,pN));

            if(possibleLikes.length()>0){
                if(parseInt(possibleLikes)>0) {
                    totalLikesPerProduct.add(possibleLikes);
                    existingProductNumbers.add(pN);
                }
            }
        }
        for(String likes:totalLikesPerProduct){
            Log.d("TotalLikes:", likes);
        }
        return totalLikesPerProduct;
    }
    private int getNumLikes(List<String> totalLikes){
        int numLikes=0;
        int newLikes;
        for(String possLikes:totalLikes){
            if(possLikes.length()!=0){
                newLikes= parseInt(possLikes);
                numLikes+=newLikes;
            }
        }
        return numLikes;
    }

}
