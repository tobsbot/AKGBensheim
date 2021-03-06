package de.tobiaserthal.akgbensheim.preferences.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import de.tobiaserthal.akgbensheim.R;
import de.tobiaserthal.akgbensheim.backend.preferences.PreferenceProvider;
import de.tobiaserthal.akgbensheim.base.adapter.AdapterClickHandler;
import de.tobiaserthal.akgbensheim.base.adapter.BaseViewHolder;

public class SyncPreferenceAdapter extends RecyclerView.Adapter<SyncPreferenceAdapter.ItemViewHolder> {
    private Context context;
    private AdapterClickHandler clickHandler;
    private ArrayList<PreferenceProvider.PeriodicSyncPreference> periodicSyncs;

    public SyncPreferenceAdapter(Context context, Collection<PreferenceProvider.PeriodicSyncPreference> periodicSyncs) {
        this.context = context;
        this.periodicSyncs = new ArrayList<>();
        this.periodicSyncs.addAll(periodicSyncs);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.preference_sync_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return periodicSyncs != null ?
                periodicSyncs.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        PreferenceProvider.PeriodicSyncPreference sync = periodicSyncs.get(position);
        return sync != null ? sync.getWhich() : -1L;
    }

    public void setClickHandler(AdapterClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    public PreferenceProvider.PeriodicSyncPreference getItem(int position) {
        return periodicSyncs != null ?
                periodicSyncs.get(position) : null;
    }

    public ArrayList<PreferenceProvider.PeriodicSyncPreference> getItems() {
        return periodicSyncs;
    }

    public Context getContext() {
        return context;
    }

    class ItemViewHolder extends BaseViewHolder<PreferenceProvider.PeriodicSyncPreference>
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        private TextView txtTitle;
        private TextView txtFrequency;
        private SwitchCompat switchCompat;
        private LinearLayout textContainer;

        public ItemViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(android.R.id.text1);
            txtFrequency = (TextView) itemView.findViewById(android.R.id.text2);
            switchCompat = (SwitchCompat) itemView.findViewById(android.R.id.button1);
            textContainer = (LinearLayout) itemView.findViewById(android.R.id.widget_frame);

            switchCompat.setOnCheckedChangeListener(this);
            textContainer.setOnClickListener(this);
        }

        @Override
        public void bind(PreferenceProvider.PeriodicSyncPreference sync) {
            txtTitle.setText(context.getString(R.string.pref_title_sync_item_title, sync.getTitle()));
            txtFrequency.setText(context.getString(R.string.pref_title_sync_item_frequency, sync.getSubtitle()));
            switchCompat.setChecked(sync.isEnabled());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getItem(getAdapterPosition()).setEnabled(isChecked);
        }

        @Override
        public void onClick(View v) {
            if(clickHandler != null) {
                clickHandler.onClick(v, getAdapterPosition(), getItemId());
            }
        }
    }
}
