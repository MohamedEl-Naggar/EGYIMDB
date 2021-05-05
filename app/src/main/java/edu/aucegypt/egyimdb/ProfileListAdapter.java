package edu.aucegypt.egyimdb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<edu.aucegypt.egyimdb.ProfileListAdapter.ExampleViewHolder> implements Filterable {
    private ArrayList<ProfileCard> exampleListFull;
    private ArrayList<ProfileCard> mExampleList;

    private OnItemClickListener mListener;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public ExampleViewHolder (View itemView, OnItemClickListener listener)
        {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.not_a_member);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextView3 = itemView.findViewById(R.id.textView5);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    public ProfileListAdapter(ArrayList<ProfileCard> exampleList)
    {
        mExampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ProfileCard currentItem = mExampleList.get(position);
        String str = String.valueOf(position+1)+"- ";
        holder.mTextView1.setText(str+currentItem.getMovieName());
        holder.mTextView2.setText(currentItem.getDuration());
        holder.mTextView3.setText(currentItem.getRevenue());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    @Override
    public Filter getFilter()
    {
        return examplefilter;
    }

    public Filter examplefilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ProfileCard> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(exampleListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ProfileCard item : exampleListFull)
                {
                    if (item.getMovieName().toLowerCase().contains(filterPattern)) //|| item.getUserInstrument().toLowerCase().contains(filterPattern) || item.getUserProf().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mExampleList.clear();
            mExampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}