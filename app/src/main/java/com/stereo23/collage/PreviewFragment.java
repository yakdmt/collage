package com.stereo23.collage;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.stereo23.collage.utilities.DownloadTask;
import com.stereo23.collage.utilities.DownloadTaskResponse;
import com.stereo23.collage.utilities.StaticMethods;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PreviewFragment extends Fragment {

    private ArrayList<String> checkedImages;
    public DownloadTaskResponse delegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkedImages = this.getArguments().getStringArrayList("images");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preview, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        StaticMethods.cleanAppFolder();
        delegate = new DownloadTaskResponse() {
            @Override
            public void onDownloadComplete() {
                ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView);
                List<File> files = StaticMethods.getListFiles(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/collage/"));
                Bitmap combineImages = StaticMethods.mergeFilesToBitmap(files);
                imageView.setImageBitmap(combineImages);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/collage/collage.jpg");
                    combineImages.compress(Bitmap.CompressFormat.JPEG, 90, out);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new DownloadTask(getActivity(), delegate).execute(checkedImages);
        Button sendButton = (Button) getActivity().findViewById(R.id.sendEmailButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file;
                    ArrayList<Uri> uris = new ArrayList<Uri>();
                    Uri u;
                    Intent emailSession = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    emailSession.setType("images/*");
                    FileWriter fw;
                    BufferedWriter bw;
                    try {
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/collage/collage.jpg");
                        Log.d(MainActivity.TAG,"file path is "+file.getAbsolutePath());
                        if (file.exists()) {
                            Log.d(MainActivity.TAG,"file exists");
                            Uri u1 = Uri.fromFile(file);
                            uris.add(u1);
                            emailSession.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

                            startActivity(emailSession);
                        }
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


}
