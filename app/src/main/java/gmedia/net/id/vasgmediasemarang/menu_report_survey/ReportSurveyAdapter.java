package gmedia.net.id.vasgmediasemarang.menu_report_survey;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.SimpleObjectModel;

public class ReportSurveyAdapter extends RecyclerView.Adapter<ReportSurveyAdapter.ReportSurveyViewHolder> {

    private Activity activity;
    private List<String> listHeader;
    private HashMap<String, List<ReportModel>> listForm;
    private List<SimpleObjectModel> listSatuan = new ArrayList<>();

    ReportSurveyAdapter(Activity activity, List<String> listHeader, HashMap<String, List<ReportModel>> listForm){
        this.activity = activity;
        this.listHeader = listHeader;
        this.listForm = listForm;
    }

    void setListSatuan(List<SimpleObjectModel> listSatuan){
        this.listSatuan = listSatuan;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportSurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReportSurveyViewHolder(LayoutInflater.from(activity).
                inflate(R.layout.item_report_header, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReportSurveyViewHolder holder, int position) {
        String h = listHeader.get(position);
        holder.txt_header.setText(h);
        holder.rv_fields.setItemAnimator(new DefaultItemAnimator());
        holder.rv_fields.setLayoutManager(new LinearLayoutManager(activity));
        holder.rv_fields.setAdapter(new ReportSurveyChildAdapter(listForm.get(h)));
    }

    @Override
    public int getItemCount() {
        return listHeader.size();
    }

    class ReportSurveyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_header;
        RecyclerView rv_fields;

        ReportSurveyViewHolder(View itemView) {
            super(itemView);
            txt_header = itemView.findViewById(R.id.txt_header);
            rv_fields = itemView.findViewById(R.id.rv_fields);
        }
    }

    class ReportSurveyChildAdapter extends RecyclerView.Adapter<ReportSurveyChildAdapter.ReportSurveyChildViewHolder>{
        private final int TYPE_SELECT = 19;
        private List<ReportModel> listReport;

        ReportSurveyChildAdapter(List<ReportModel> listReport){
            this.listReport = listReport;
        }

        @Override
        public int getItemViewType(int position) {
            if(listReport.get(position) instanceof ReportSelectModel){
                return TYPE_SELECT;
            }
            else{
                return super.getItemViewType(position);
            }
        }

        @NonNull
        @Override
        public ReportSurveyChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType == TYPE_SELECT){
                return new ReportSurveySelectViewHolder(LayoutInflater.from(activity).
                        inflate(R.layout.item_report_spinner, parent, false));
            }
            else{
                return new ReportSurveyTextViewHolder(LayoutInflater.from(activity).
                        inflate(R.layout.item_report_text, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ReportSurveyChildViewHolder holder, int position) {
            final ReportModel report = listReport.get(position);

            holder.txt_judul.setText(report.getNama());
            if(report.is_unit()){
                holder.spn_satuan.setVisibility(View.VISIBLE);
                ArrayAdapter<SimpleObjectModel> adapter = new ArrayAdapter<>
                        (activity, android.R.layout.simple_spinner_item, listSatuan);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.spn_satuan.setAdapter(adapter);
                holder.spn_satuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        report.setUnit_id(listSatuan.get(position).getId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            else{
                holder.spn_satuan.setVisibility(View.GONE);
            }

            if(holder instanceof ReportSurveySelectViewHolder){
                final ReportSelectModel reportSelect = (ReportSelectModel) report;

                if(!reportSelect.isLoaded()){
                    loadReportSelectEntries(reportSelect.getApi_url(),
                            holder.getAdapterPosition(), ((ReportSurveySelectViewHolder) holder).spn_value);
                }
                else{
                    ArrayAdapter<SimpleObjectModel> adapter = new ArrayAdapter<>
                            (activity, android.R.layout.simple_spinner_item, reportSelect.getList());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ((ReportSurveySelectViewHolder) holder).spn_value.setAdapter(adapter);
                    ((ReportSurveySelectViewHolder) holder).spn_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            report.setValue(reportSelect.getList().get(position).getId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            else if(holder instanceof ReportSurveyTextViewHolder){
                ((ReportSurveyTextViewHolder)holder).txt_value.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        report.setValue(s.toString());
                    }
                });
            }
        }

        private void loadReportSelectEntries(String url, final int adapterPosition, final AppCompatSpinner spn_value){
            ApiVolley request = new ApiVolley(activity, new JSONObject(), "POST",
                    url, "", "",
                    0, new ApiVolley.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try{
                        JSONObject jsonres = new JSONObject(result);
                        if(jsonres.getJSONObject("metadata").getInt("status") == 200){
                            JSONArray response = jsonres.getJSONArray("response");
                            List<SimpleObjectModel> listSelect = new ArrayList<>();

                            for(int i = 0; i < response.length(); i++){
                                JSONObject object = response.getJSONObject(i);
                                listSelect.add(new SimpleObjectModel(object.getString("id"),
                                        object.getString("text")));
                            }

                            ((ReportSelectModel)listReport.get(adapterPosition)).
                                    setListSelect(listSelect);
                            ((ReportSelectModel)listReport.get(adapterPosition)).
                                    setLoaded(true);

                            ArrayAdapter<SimpleObjectModel> adapter = new ArrayAdapter<>
                                    (activity, android.R.layout.simple_spinner_item, listSelect);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spn_value.setAdapter(adapter);
                            spn_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    listReport.get(adapterPosition).setValue(((ReportSelectModel)listReport.
                                            get(adapterPosition)).getList().get(position).getId());
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            notifyItemChanged(adapterPosition);
                        }
                        else{
                            Toast.makeText(activity, jsonres.getJSONObject("metadata").
                                    getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e){
                        Log.e(LinkURL.TAG, e.getMessage());
                    }
                }

                @Override
                public void onError(String result) {
                    Log.d(LinkURL.TAG, result);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listReport.size();
        }

        class ReportSurveyChildViewHolder extends RecyclerView.ViewHolder{

            TextView txt_judul;
            AppCompatSpinner spn_satuan;
            ReportSurveyChildViewHolder(View itemView) {
                super(itemView);
                txt_judul = itemView.findViewById(R.id.txt_judul);
                spn_satuan = itemView.findViewById(R.id.spn_satuan);
            }
        }

        class ReportSurveyTextViewHolder extends ReportSurveyChildViewHolder {

            EditText txt_value;
            ReportSurveyTextViewHolder(View itemView) {
                super(itemView);
                txt_value = itemView.findViewById(R.id.txt_value);
            }
        }

        class ReportSurveySelectViewHolder extends ReportSurveyChildViewHolder {

            AppCompatSpinner spn_value;
            ReportSurveySelectViewHolder(View itemView) {
                super(itemView);
                spn_value = itemView.findViewById(R.id.spn_value);
            }
        }
    }
}
