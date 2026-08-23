package com.example.duancuoiky_25th2523;
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
    // ... các khai báo cũ (ROWS, COLS, buttons)
    private int[][] board = new int[ROWS][COLS]; // Mảng lưu trạng thái logic
    private final int NUM_MINES = 10; // Đặt 10 quả mìn cho bàn 8x8
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);
        initBoardUI();
    }
    private void placeMines() {
        // Khởi tạo mảng toàn số 0 (trống)
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = 0;
            }
        }

        // Rải mìn ngẫu nhiên
        int minesPlaced = 0;
        while (minesPlaced < NUM_MINES) {
            int r = (int) (Math.random() * ROWS);
            int c = (int) (Math.random() * COLS);

            // Nếu ô này chưa có mìn thì mới đặt
            if (board[r][c] != -1) {
                board[r][c] = -1;
                minesPlaced++;
            }
        }
    }
    private void calculateNumbers() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                // Bỏ qua nếu ô này đã là mìn
                if (board[r][c] == -1) continue;

                int mineCount = 0;
                // Duyệt 8 ô xung quanh
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int nr = r + i;
                        int nc = c + j;
                        // Kiểm tra xem ô xung quanh có hợp lệ (không vượt quá mảng) và có mìn không
                        if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS && board[nr][nc] == -1) {
                            mineCount++;
                        }
                    }
                }
                // Gán số mìn đếm được vào ô đó
                board[r][c] = mineCount;
            }
        }
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