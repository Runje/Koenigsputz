package com.apps.koenig.thomas.koenigsputz.View.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.apps.koenig.thomas.koenigsputz.R;
import com.example.communication.model.CleanTask;

import java.util.List;

/**
 * Created by Thomas on 05.02.2017.
 */

public class CleanTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ListItem> cleanTasks;

    public CleanTaskAdapter(List<ListItem> cleanTasks) {
        this.cleanTasks = cleanTasks;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ListItem.TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listheader, parent, false);

            return new HeaderViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task, parent, false);

            return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_HEADER) {
            HeaderItem headerItem = (HeaderItem) cleanTasks.get(position);
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.text.setText(headerItem.header);
        } else {
            Item item = (Item) cleanTasks.get(position);
            CleanTask cleanTask = item.cleanTask;
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.difficulty.setNumStars(cleanTask.getDifficulty());
            itemViewHolder.duration.setText(cleanTask.getDurationInMin() + " min");
            itemViewHolder.name.setText(cleanTask.getName());
            itemViewHolder.responsible.setText(cleanTask.getResponsible());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return cleanTasks.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return cleanTasks.size();
    }

    public void setCleanTasks(List<ListItem> cleanTasks) {
        this.cleanTasks = cleanTasks;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView duration;
        private final TextView responsible;
        private final RatingBar difficulty;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textName);
            duration = (TextView) itemView.findViewById(R.id.textDuration);
            responsible = (TextView) itemView.findViewById(R.id.textResponsible);
            difficulty = (RatingBar) itemView.findViewById(R.id.rating);
        }
    }

    public class HeaderViewHolder extends ItemViewHolder {
        private final TextView text;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.textHeader);
        }
    }
}
