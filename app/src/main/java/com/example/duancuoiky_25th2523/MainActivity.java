package com.example.minesweeper; // Đảm bảo giữ đúng tên package của bạn

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;

public class MainActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private final int ROWS = 8;
    private final int COLS = 8;

    // Mảng 2 chiều để lưu trữ giao diện của 64 ô
    private Button[][] buttons = new Button[ROWS][COLS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);
        initBoardUI();
    }

    private void initBoardUI() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Button btn = new Button(this);

                // Cấu hình kích thước cho từng ô (Ví dụ: 120x120 pixel)
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 120;
                params.height = 120;
                params.setMargins(2, 2, 2, 2); // Tạo kẽ hở giữa các ô
                btn.setLayoutParams(params);

                btn.setBackgroundColor(0xFFDDDDDD); // Màu xám nhạt

                // Lưu nút vào mảng 2 chiều và thêm vào màn hình
                buttons[i][j] = btn;
                gridLayout.addView(btn);
            }
        }
    }
}