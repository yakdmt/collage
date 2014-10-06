package com.stereo23.collage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainFragment extends Fragment {

    Button button;
    EditText editText;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

    @Override
    public void onResume() {
        super.onResume();
        editText = (EditText) getActivity().findViewById(R.id.editText);
        button = (Button) getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText()!=null){
                    Bundle bundle = new Bundle();
                    bundle.putString("username",editText.getText().toString());
                    PhotoPickerFragment fragment = new PhotoPickerFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment).addToBackStack(null)
                            .commit();
                } else{
                    Toast.makeText(getActivity(),getActivity().getString(R.string.username_is_empty),Toast.LENGTH_SHORT);
                }
            }
        });


    }
}
