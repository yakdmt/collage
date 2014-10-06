package com.stereo23.collage;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.stereo23.collage.utilities.PhotoAdapter;
import com.stereo23.collage.utilities.SearchTask;
import com.stereo23.collage.utilities.SearchTaskResponse;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Username on 20.09.2014.
 */
public class PhotoPickerFragment extends Fragment {

    String username;
    public SearchTaskResponse delegate;
    GridView gridView;
    boolean[] isCheckedArray;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        username = bundle.getString("username");
        delegate = new SearchTaskResponse() {
            @Override
            public void processFinish(ArrayList<URL> output) {
                if (output == null){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.user_not_found), Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Log.d(MainActivity.TAG,output.toString());
                    PhotoAdapter adapter = new PhotoAdapter(getActivity(), output);
                    GridView gridView = (GridView) getActivity().findViewById(R.id.gridView);
                    gridView.setAdapter(adapter);
                    isCheckedArray = new boolean[adapter.getCount()];
                    Arrays.fill(isCheckedArray, Boolean.FALSE);
                }
            }
        };
        new SearchTask(getActivity(), delegate).execute(username);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photopicker, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        gridView = (GridView) getActivity().findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isCheckedArray[position]){
                    view.setBackgroundColor(Color.TRANSPARENT);
                    isCheckedArray[position]=false;
                } else {
                    view.setBackgroundColor(Color.GREEN);
                    isCheckedArray[position]=true;
                }
            }
        });
        Button getCollageButton = (Button) getActivity().findViewById(R.id.getCollage);
        getCollageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoAdapter adapter = (PhotoAdapter)gridView.getAdapter();
                ArrayList<String> checkedImages = new ArrayList<String>();
                for (int i=0; i<adapter.getCount(); i++){
                    if (isCheckedArray[i]){
                        checkedImages.add(adapter.getItem(i).toString());
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images",checkedImages);
                PreviewFragment fragment = new PreviewFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();
            }
        });

    }



}
