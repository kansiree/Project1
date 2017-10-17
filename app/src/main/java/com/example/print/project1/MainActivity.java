package com.example.print.project1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener, OnMarkerClickListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    double[][] position_address = new double[2][2];
    //private static String[] Engname = {"Victory", "Phayathai", "Mo Chit"};
    private static String[][] Thainame = {{"หมอชิต","N8"},{"สะพานควาย","N7"},{"เสนาร่วม","N6"},{"อารีย์","N5"},{"สนามเป้า","N4"},{"อนุสาวรีย์ชัยสมรภูมิ","N3"},
        {"พญาไท","N2"},{"ราชเทวี","N1"},{"สยาม","CEN"},{"ชิดลม","E1"},{"เพลินจิต","E2"},
        {"นานา","E3"},{"อโศก","E4"},{"พร้อมพงษ์","E5"},{"ทองหล่อ","E6"},{"เอกมัย","E7"},
        {"พระโขนง","E8"},{"อ่อนนุช","E9"},{"บางจาก","E10"},{"ปุณณวิถี","E11"},{"อุดมสุข","E12"},
        {"บางนา","E13"},{"แบริ่ง","E14"},/*23*/
        {"สนามกีฬา","W1"},{"ราชดำริ","S1"},{"ศาลาแดง","S2"},{"ช่องนนทรี","S3"},{"ศึกษาวิทยา","S4"},{"สุรศักดิ์","S5"},
        {"สะพานตากสิน","S6"},{"กรุงธนบุรี","S7"},{"วงเวียนใหญ่","S8"},{"โพธิ์นิมิตร","S9"},{"ตลาดพลู","S10"},
        {"วุฒากาศ","S11"},{"บางหว้า","S12"}};/*13*/
    private static final double[][] position = {{13.80244,100.55375},{13.79364,100.54968},{13.789,100.548},{13.77977,100.5447},{13.77236,100.54204},{13.76276,100.53708},
            {13.75678,100.53376},{13.75161,100.53157},{13.74555,100.53462},{13.74409,100.54305},{13.74309,100.54884},
            {13.74048,100.55545},{13.73696,100.56039},{13.73044,100.5697},{13.72423,100.57858},{13.71943,100.5853},
            {13.71518,100.59122},{13.70555,100.60109},{13.69654,100.60549},{13.68925,100.60899},{13.67991,100.60957},
            {13.6681,100.60465},{13.66116,100.60191},
            {13.74647,100.52917},{13.7393,100.53947},{13.72852,100.53436},{13.72368,100.52938},{13.7235,100.5283},{13.71924,100.52161},
            {13.71879,100.51412},{13.72091,100.5029},{13.72104,100.49528},{13.7192,100.48595},{13.71424,100.47672},
            {13.71301,100.46893},{13.7207,100.45782}};

    SupportMapFragment mMap;
    TextView adress_start ;
     TextView  adress_end;
     Button search ;
    String start_in="",end_out="";
    String start_name="",end_name="";
    GoogleMap clickmark;
    Marker position_st,position_en;
    MarkerOptions station = new MarkerOptions();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mMap = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        mMap.getMapAsync(this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        adress_end = (TextView)findViewById(R.id.end_search);
        adress_start= (TextView)findViewById(R.id.start_search);
        search= (Button)findViewById(R.id.search);
        search.setOnClickListener(this);
    }
    int _count =0,color_st=0,color1_st=0,color_en=0,color1_en=0;
    int numSt=0,numEnd=0,pos=0,num=0;
    final CharSequence[] select={"ต้นทาง","ปลายทาง"};
    Marker mark;
    double h=0,g=0;
    public boolean onMarkerClick(Marker stMarker) {
                 for(int i = 0;i<position.length;i++){
                if(stMarker.getPosition().longitude==position[i][1]&&stMarker.getPosition().latitude==position[i][0]){
                    if((h==stMarker.getPosition().latitude&&g==stMarker.getPosition().longitude)){
                        _count++;
                    }
                    else {
                        _count=0;
                    }
                    h=stMarker.getPosition().latitude;
                    g=stMarker.getPosition().longitude;
                    if(_count!=0){
                    pos=i;
                    setmark(stMarker);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("คุณต้องการเลือกสถานี  "+Thainame[pos][0]+"  เป็นต้นทางหรือปลายทาง");
                    dialog.setItems(select,new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface di, int id) {
                            if(id==0){
                                getmark();
                                numSt=pos;
                                if(color_st==0){
                                    position_st=getmark();
                                }
                                color_st++;
                                start(pos,getmark());
                            }
                            else {
                                numEnd=pos;
                                if(color_en==0){
                                    position_en=getmark();
                                }
                                color_en++;
                                end(pos,getmark());
                            }
                        }
                    });
                    AlertDialog dialogbuil = dialog.create();
                    dialogbuil.show();
                }
                else {//stMarker.showInfoWindow();
                        if (stMarker.isInfoWindowShown()) {
                            stMarker.hideInfoWindow();
                        } else {
                            stMarker.showInfoWindow();
                        }

                    }
                }

        }
        return true;
    }
    private void setmark(Marker mark1){
        mark=mark1;
    }
    private Marker getmark(){
        return mark;
    }
    int num_station=0;
    private void setcalnum(int start,int end){
        String check_start = Thainame[start][1];
        String check_end = Thainame[end][1];
        String numstart= check_start.substring(1,check_start.length());
        String numend= check_end.substring(1,check_end.length());
        String p1=String.valueOf(check_start.charAt(0));
        String p2= String.valueOf(check_end.charAt(0));
        int a ;
        int b ;
        if(p1.equalsIgnoreCase("C")){
             a =0;
            b = Integer.parseInt(numend);
        }
        else if (p2.equalsIgnoreCase("C")){
            b = 0;
            a = Integer.parseInt(numstart);
        }
        else {
            a = Integer.parseInt(numstart);
            b = Integer.parseInt(numend);
        }
        if(p1.equalsIgnoreCase(p2)){
            num=Math.abs(a-b);
        }
        else {num=a+b;}
    }
    private int num(String start,String end){
        int num=0;
        num=(Math.abs(Integer.parseInt(start.substring(1,start.length())))-(Integer.parseInt(end.substring(1,end.length()))));
        return num;
    }
    private int getcalnum(){
        return num;
    }
    private int cal(int start,int end){
        String check_start = Thainame[start][1];
        String check_end = Thainame[end][1];
        int price_BTS=0,price_start=0,num_Station=0;
        num_Station = getcalnum();
               num_station=num_Station;
        String []state = {"E10","E11","E12","E13","E14","S9","S10","S11","S12"};
        for (int i=0;i<state.length;i++){
            if(check_start.equalsIgnoreCase(state[i])||check_end.equalsIgnoreCase(state[i])){
                    price_start=10;
                if(check_end.substring(0,1).equalsIgnoreCase("E")){
                   num_Station=Math.abs(9-Integer.parseInt(check_end.substring(1,check_end.length()))-num(check_start,check_end));
                }
                else if(check_start.substring(0,1).equalsIgnoreCase("E")){
                    num_Station=Math.abs(9-Integer.parseInt(check_start.substring(1,check_start.length()))-num(check_start,check_end));
                }
            else if(check_start.substring(0,1).equalsIgnoreCase("S")){
                    num_Station=Math.abs(8-Integer.parseInt(check_start.substring(1,check_start.length()))-num(check_start,check_end));
               }else if(check_end.substring(0,1).equalsIgnoreCase("S")){
                    num_Station=Math.abs(8-Integer.parseInt(check_end.substring(1,check_end.length()))-num(check_start,check_end));
                }
                if(check_end.equalsIgnoreCase("E9")||check_start.equalsIgnoreCase("E9")){
                    price_BTS=10;
                    break;}
            }
        }
        for (int j=0;j<state.length;j++){
            if(check_start.equalsIgnoreCase(state[j])){start_in = state[j];break;}
            else {start_in="A";}
        }
        for(int k =0;k<state.length;k++){
            if(check_end.equalsIgnoreCase(state[k])){end_out = state[k];break;}
            else {end_out="B";}
        }
        if(start_in.substring(0,1).equals(end_out.substring(0,1)))
        {
                price_BTS=10;
            }
        else if(!(start_in.equalsIgnoreCase("A"))&&check_start.equalsIgnoreCase("E9")){
            price_BTS=10;
        }
        else if(!(start_in.equalsIgnoreCase("A"))&&check_end.equalsIgnoreCase("E9")){
            price_BTS=10;
        }
        else if(!(end_out.equalsIgnoreCase("B"))&&check_start.equalsIgnoreCase("E9")){
            price_BTS=10;
        }
        else if(!(end_out.equalsIgnoreCase("B"))&&check_end.equalsIgnoreCase("E9")){
            price_BTS=10;
        }
        else if(!(start_in.equalsIgnoreCase("A"))&&check_start.equalsIgnoreCase("S8")){
            price_BTS=10;
        }
        else if(!(start_in.equalsIgnoreCase("A"))&&check_end.equalsIgnoreCase("S8")){
            price_BTS=10;
        }
        else if(!(end_out.equalsIgnoreCase("B"))&&check_start.equalsIgnoreCase("S8")){
            price_BTS=10;
        }
        else if(!(end_out.equalsIgnoreCase("B"))&&check_end.equalsIgnoreCase("S8")){
            price_BTS=10;
        }
        else {
                    if(num_Station<=1){price_BTS=15+price_start;}
                    else if(num_Station==2){price_BTS=22+price_start;}
                    else if(num_Station==3){price_BTS=25+price_start;}
                    else if(num_Station==4){price_BTS=28+price_start;}
                    else if(num_Station==5){price_BTS=31+price_start;}
                    else if(num_Station==6){price_BTS=34+price_start;}
                    else if(num_Station==7){price_BTS=37+price_start;}
                    else if(num_Station>=8){price_BTS=42+price_start;}
                }
            return price_BTS;
    }
    private int way(){
        int time =Math.abs(num_station)*2;
        GoogleDirection.withServerKey("AIzaSyBd0bxqNbYmnkNjWfSgO8tzY3b4lH_WCZo")
                .from(new LatLng(position[numSt][0],position[numSt][1]))
                .to(new LatLng(position[numEnd][0],position[numEnd][1]))
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });
    return time;}
    public String start(int i ,Marker stMarker ){
        start_name = Thainame[i][0];
        setstart(start_name);
        adress_start.setText(start_name+"   "+Thainame[i][1]);
        stMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        if(position_st.getPosition().latitude!=stMarker.getPosition().latitude&&color_st!=1){
            if(color1_st<=22){
                position_st.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
            else {
                position_st.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
        }
        color1_st=i;
        position_st=stMarker;
        return start_name;
    }
    public String end(int i,Marker stMarker){
        end_name = Thainame[i][0];
        setend(end_name);
        adress_end.setText(end_name+"   "+Thainame[i][1]);
        stMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        if(position_en.getPosition().latitude!=stMarker.getPosition().latitude&&color_en!=1){
            if(color1_en<=22){
                position_en.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
            else {
                position_en.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }

        }
        color1_en=i;
        position_en=stMarker;
        return end_name;
    }
    public void setstart(String name){
        start_name=name;
    }
    public String getstart(){
        return start_name;
    }
    public void setend(String name){
        end_name=name;
    }
    public String getend(){
        return end_name;
    }
    public double[][] position_adress(String start, String end) {
        List<android.location.Address> ads, ade;
        Geocoder coder = new Geocoder(this);
        try {
            ads = coder.getFromLocationName(start, 5);
            ade = coder.getFromLocationName(end, 5);
            if (start == null || end == null) {

                return null;
            }
            android.location.Address location = ads.get(0);
            android.location.Address location_end = ade.get(0);
            position_address[0][0] = location.getLatitude();
            position_address[0][1] = location.getLongitude();
            position_address[1][0] = location_end.getLatitude();
            position_address[1][1] = location_end.getLongitude();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return position_address;
    }

    public boolean googlesevice() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "not connect sevice", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        clickmark=googleMap;
        for (int i = 0; i < position.length; i++) {
            try {
             clickmark.setOnMarkerClickListener(this);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
           d(i);
            clickmark.addMarker(station);
        }
       googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13.76256,100.53708), 1));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 200, null);
    }

    private void d(int la) {
        if(la!=2&&la!=27)
        { station.position(new LatLng(position[la][0], position[la][1])).title(Thainame[la][0]).snippet(Thainame[la][0]);
            if(la<=22){
                station.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            else {
                station.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
        }

    }
    @Override
    public void onClick(View view) {
        if(search.isClickable()){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            setcalnum(numSt,numEnd);
            dialog.setTitle("ข้อมูลการเดินทาง");
            dialog.setMessage("ต้นทาง                  "+getstart()+"\nปลายทาง             "+getend()+"\nราคา                     "+cal(numSt,numEnd)+" บาท\nเวลาการเดินทาง  "+way()+"  นาที").setPositiveButton(" OK ",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface di, int id) {
                    di.cancel();
                }
            });
            AlertDialog dialogbuil = dialog.create();
            dialogbuil.show();
        }
    }
}


