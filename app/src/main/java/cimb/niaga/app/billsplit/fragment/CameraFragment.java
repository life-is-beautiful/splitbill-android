package cimb.niaga.app.billsplit.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import cimb.niaga.app.billsplit.R;
import cimb.niaga.app.billsplit.activities.CameraActivity;
import cimb.niaga.app.billsplit.adapter.OwedAdapter;
import cimb.niaga.app.billsplit.adapter.PriceAdapter;
import cimb.niaga.app.billsplit.adapter.ViewPagerHomeAdapter;
import cimb.niaga.app.billsplit.adapter.ViewPagerPersonAdapter;
import cimb.niaga.app.billsplit.corecycle.GeneralizeImage;
import cimb.niaga.app.billsplit.corecycle.MyAPIClient;
import cimb.niaga.app.billsplit.corecycle.MyPicasso;
import cimb.niaga.app.billsplit.corecycle.RoundImageTransformation;

import static cimb.niaga.app.billsplit.activities.CameraActivity.ALLOW_KEY;

/**
 * Created by Denny on 1/14/2017.
 */

public class CameraFragment extends Fragment {
    Uri mCapturedImageURI;
    private final int RESULT_CAMERA = 200;
    public static final int RESULT_OK   = -1;
    ImageView img_bill;
    TabLayout tabs;
    ViewPager pager;
    ListView listPrice;
    PriceAdapter priceAdapter;

    public static ArrayList<String> list_price = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(container != null) {
            container.removeAllViews();
        }

        View view = inflater.inflate(R.layout.camera_fragment, container, false);

        list_price.add("Rp 75.000,00");
        list_price.add("Rp 15.000,00");
        list_price.add("Rp 65.000,00");
        list_price.add("Rp 25.000,00");
        list_price.add("Rp 20.000,00");
        list_price.add("Rp 6.000,00");

        listPrice = (ListView) view.findViewById(R.id.list_price);

        priceAdapter = new PriceAdapter(getActivity());
        listPrice.setAdapter(priceAdapter);
        img_bill = (ImageView) view.findViewById(R.id.img_bill);
        tabs = (TabLayout) view.findViewById(R.id.tabs);
        pager = (ViewPager) view.findViewById(R.id.pager);
        ViewPagerPersonAdapter adapter = new ViewPagerPersonAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        String fileName = "temp.jpg";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);

        mCapturedImageURI =getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        startActivityForResult(intent, RESULT_CAMERA);

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchContent(Fragment mFragment,String fragName,Boolean isBackstack) {
        if(isBackstack){
            Log.d("backstack", "masuk");
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, mFragment, fragName)
                    .addToBackStack(null)
                    .commit();
        }
        else {
            Log.d("bukan backstack","masuk");
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, mFragment, fragName)
                    .commit();

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case RESULT_CAMERA:
                if(resultCode == RESULT_OK && mCapturedImageURI!=null){
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(mCapturedImageURI, projection, null, null, null);
                    cursor.moveToFirst();
                    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    String filePath = cursor.getString(column_index_data);

                    File photoFile = new File(filePath);
                    final GeneralizeImage mGI = new GeneralizeImage(getActivity(),filePath);
                    //getOrientationImage();
                    img_bill.setVisibility(View.VISIBLE);
                    setImageProfPic(photoFile);
//                    btn_Rescan.setVisibility(View.VISIBLE);
//                    btnDone.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            uploadFileToServer(mGI.Convert());
//                        }
//                    });
                }
                else{
                    Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    public void setImageProfPic(File filenya){
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.user_unknown_menu);
        RoundImageTransformation roundedImage = new RoundImageTransformation(bm);

        Picasso mPic;
        if(MyAPIClient.PROD_FLAG_ADDRESS)
            mPic = MyPicasso.getImageLoader(getActivity());
        else
            mPic= Picasso.with(getActivity());

        if(!filenya.exists()){
            mPic.load(R.mipmap.user_unknown_menu)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(roundedImage)
                    .fit().centerInside()
//                    .placeholder(R.anim.progress_animation)
                    .transform(new RoundImageTransformation(getActivity())).into(img_bill);
            Bitmap myBitmap = BitmapFactory.decodeFile(filenya.getAbsolutePath());
            img_bill.setImageBitmap(myBitmap);
        }
        else {
            Bitmap myBitmap = BitmapFactory.decodeFile(filenya.getAbsolutePath());
            img_bill.setImageBitmap(myBitmap);
        }

    }
}
