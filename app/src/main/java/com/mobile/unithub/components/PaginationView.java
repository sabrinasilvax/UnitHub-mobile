package com.mobile.unithub.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.unithub.R;

public class PaginationView extends LinearLayout {

    private Button prevPageButton, nextPageButton;
    private TextView currentPageTextView;

    private int currentPage = 1;
    private int totalPages = 1;

    private OnPageChangeListener onPageChangeListener;

    public PaginationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaginationView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.pagination_view, this, true);

        prevPageButton = findViewById(R.id.prevPageButton);
        nextPageButton = findViewById(R.id.nextPageButton);
        currentPageTextView = findViewById(R.id.currentPageTextView);

        prevPageButton.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                updateUI();
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageChanged(currentPage);
                }
            }
        });

        nextPageButton.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                updateUI();
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageChanged(currentPage);
                }
            }
        });

        updateUI();
    }

    private void updateUI() {
        currentPageTextView.setText("Página " + currentPage);
        prevPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(currentPage < totalPages);
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        updateUI();
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        updateUI();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.onPageChangeListener = listener;
    }

    public interface OnPageChangeListener {
        void onPageChanged(int page);
    }
}