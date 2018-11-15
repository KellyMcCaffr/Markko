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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class busDataBarChart extends Activity {
    int i;
    BarChart chart;
    BarDataSet dataSet;
    String enteredUsername;
    busProductsInformationDatabaseManager busProdInfo;
    ArrayList<String> productLikesData;
    ArrayList<String> existingProdNums;
    int previousProd=4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_data_bar_chart);
        Bundle paramsPassed;
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        chart= (BarChart) findViewById(R.id.bar_chart);
        busProdInfo=new busProductsInformationDatabaseManager(this,null,null,1);
        getTotalLikes();
        buildBarChart();
    }

    private void tryLikesForProduct(String productNumber){

        String possibleLikes;
        possibleLikes=busProdInfo.getTotalLikes(enteredUsername,busProdInfo.getProductNameByIdentifier(enteredUsername,productNumber));

        if(possibleLikes.length()>0){
            if(Integer.parseInt(possibleLikes)>0) {
                productLikesData.add(possibleLikes);
                existingProdNums.add(productNumber);
            }
        }
    }

    private void getTotalLikes(){
        productLikesData=new ArrayList<>();
        existingProdNums=new ArrayList<>();
        List<String> pList= Arrays.asList("1","2","3","4","5","6");
        for(String pN:pList){
            tryLikesForProduct(pN);
        }
    }

    public void buildBarChart(){

        int likesInt;
        i=3;
        final int numProds=productLikesData.size();
        final int maxGraphVal=36;
        ArrayList<BarEntry> entries = new ArrayList<>();

        for(String productLikes: productLikesData) {
            likesInt=Integer.parseInt(productLikes);
            entries.add(new BarEntry(i,likesInt));
            i+=6;
        }

        if(productLikesData.size()>0) {

            dataSet = new BarDataSet(entries, "1");
            BarData data = new BarData(dataSet);

            dataSet.setValueTextSize(0);

            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            chart.setData(data);
            chart.setScaleEnabled(false);

            chart.getBarData().setBarWidth(6);

            chart.setTouchEnabled(false);

            XAxis axisX=chart.getXAxis();
            axisX.setDrawLabels(true);
            axisX.setTextSize(16);
            YAxis axisRight=chart.getAxisRight();

            axisRight.setEnabled(false);

            YAxis axisLeft=chart.getAxisLeft();

            axisLeft.setGranularity(1);
            axisLeft.setTextSize(18);
            axisLeft.setTextColor(Color.BLACK);
            axisX.setGranularity(6);
            axisX.setLabelCount(7,true);

            axisX.setAxisMinValue(0);
            axisX.setAxisMaxValue(maxGraphVal);

            axisX.setCenterAxisLabels(true);
            axisX.setGranularity(maxGraphVal/numProds);

            axisX.setValueFormatter(new AxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    if(previousProd!=6) {
                        previousProd+=1;
                    }
                    else{
                        previousProd=1;
                    }
                    if(previousProd>productLikesData.size()) {
                        return "";
                    }
                    else{
                        String productName=busProdInfo.getProductNameByIdentifier(enteredUsername,existingProdNums.get(previousProd-1));
                        if(productName.length()>4) {
                            return productName.substring(0, 5);
                        }
                        return productName;
                    }
                }
                @Override
                public int getDecimalDigits() {
                    return 0;
                }
            });

            axisX.setPosition(XAxis.XAxisPosition.BOTTOM);
            axisX.setDrawGridLines(false);


            chart.getAxisLeft().setDrawGridLines(false);

        }
        chart.setDescription("");



    }

}
