package bob.markkouserloginsignup;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class prodDatPage extends Activity {

    TextView productNameView;
    TextView totalLikesView;
    TextView totalDislikesView;
    TextView percentLikesView;
    TextView ethnicityView;
    TextView religionView;
    TextView ageView;
    TextView educationView;
    TextView genderView;

    String enteredUsername;
    String planName;
    String productNumber;
    String productName;

    String totalLikes;
    String totalDislikes;

    busProductsInformationDatabaseManager busProdDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_dat_page);
        productNameView=(TextView)findViewById(R.id.pName);
        totalLikesView=(TextView)findViewById(R.id.totLikes);
        totalDislikesView=(TextView)findViewById(R.id.totDislikes);
        percentLikesView=(TextView)findViewById(R.id.percLikes);
        ethnicityView=(TextView)findViewById(R.id.ethnicity);
        religionView=(TextView)findViewById(R.id.religion);
        ageView=(TextView)findViewById(R.id.age);
        educationView=(TextView)findViewById(R.id.education);
        genderView=(TextView)findViewById(R.id.gender);

        busProdDB=new busProductsInformationDatabaseManager(this,null,null,1);


        Bundle paramsPassed;
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        planName = paramsPassed.getString("planName");
        productNumber = paramsPassed.getString("productNumber");
        productName=busProdDB.getProductNameByIdentifier(enteredUsername,productNumber);
        totalLikes=busProdDB.getTotalLikes(enteredUsername,productName);
        totalDislikes=busProdDB.getTotalDislikes(enteredUsername,productName);

        setNameView();
        setTotalLikesView();
        setTotalDislikesView();
        setLikesPercentView();
        setEthnicityView();
        setReligionView();
        setAgeView();
        setEducationView();
        setGenderView();
    }

    private void setNameView(){

        Log.d("Product number:", productNumber);
        Log.d("Prod name:", productName);
        productNameView.setText(productName);
    }

    private void setTotalLikesView(){

        totalLikesView.setText(totalLikes);
    }

    private void setTotalDislikesView(){
        totalDislikesView.setText(totalDislikes);
    }

    private void setLikesPercentView(){
        //Need both likes and dislikes, this fixed a bug in program
        if(totalLikes.length()>0) {
            float totLikesAsFloat = Float.parseFloat(totalLikes);
            float totDislikesAsFloat;
            if(totalDislikes.length()>0) {
                totDislikesAsFloat = Float.parseFloat(totalDislikes);
            }
            else{
                totDislikesAsFloat=0;
            }
            float likesPercent = (totLikesAsFloat / (totLikesAsFloat + totDislikesAsFloat)) * 100;
            percentLikesView.setText(Float.toString(likesPercent));
        }
        else{
            percentLikesView.setText("N/A");
        }

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
        ethnicityView.setText(ethnFinalString);

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
        religionView.setText(religionFinalString);

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
        ageView.setText(religionFinalString);

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
        educationView.setText(educationFinalString);

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
        genderView.setText(educationFinalString);

    }
}
