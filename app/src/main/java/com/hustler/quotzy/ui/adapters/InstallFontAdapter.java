package com.hustler.quotzy.ui.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hustler.quotzy.R;
import com.hustler.quotzy.ui.utils.Toast_Snack_Dialog_Utils;

import java.io.File;
import java.util.List;

/**
 * Created by anvaya5 on 19/01/2018.
 */
/*   Copyright [2018] [Sayi Manoj Sugavasi]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
public class InstallFontAdapter extends RecyclerView.Adapter<InstallFontAdapter.FontViewHolder> {
    Activity activity;
    List<String> zip_File_Contents_list;
    String[] font_Paths_from_source_location;
    String sourcePath;
    String destinationPath;

    public InstallFontAdapter(Activity activity, List<String> zip_File_Contents_list, String sourcePath, String destinationPath, String[] font_Paths_from_source_location) {
        this.activity = activity;
        this.zip_File_Contents_list = zip_File_Contents_list;
        this.sourcePath = sourcePath;
        this.destinationPath =destinationPath;
        checkDirectory(destinationPath);
        this.font_Paths_from_source_location=font_Paths_from_source_location;
    }

    private void checkDirectory(String finalLOcation) {
        if(new File(finalLOcation).isDirectory()){

        }else {
            new File(finalLOcation).mkdir();
        }
    }


    @Override
    public FontViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FontViewHolder(activity.getLayoutInflater().inflate(R.layout.install_font_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FontViewHolder holder, final int position) {
        holder.textView.setText(zip_File_Contents_list.get(position));
        holder.textView.setTypeface(Typeface.createFromFile(font_Paths_from_source_location[position]));
        holder.installText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(activity);
                dialog.show();
                new File(font_Paths_from_source_location[position]).renameTo(new File(destinationPath + File.separator + zip_File_Contents_list.get(position)));
                Toast_Snack_Dialog_Utils.createDialog(activity,
                        activity.getString(R.string.congratulations),
                        activity.getString(R.string.font_installed)
                        , null, activity.getString(R.string.close), new Toast_Snack_Dialog_Utils.Alertdialoglistener() {
                            @Override
                            public void onPositiveselection() {
                            }

                            @Override
                            public void onNegativeSelection() {

                            }
                        });
                dialog.dismiss();
//                Toast_Snack_Dialog_Utils.show_ShortToast(activity, activity.getString(R.string.font_installed));
            }
        });
    }

    @Override
    public int getItemCount() {
        return zip_File_Contents_list.size() <= 0 ? 0 : zip_File_Contents_list.size();
    }

    public class FontViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView installText;

        public FontViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);
            installText = itemView.findViewById(R.id.install_text);
        }
    }
}
