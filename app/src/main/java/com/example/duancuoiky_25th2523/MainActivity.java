package com.example.duancuoiky_25th2523;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;

public class MainActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private final int ROWS = 8;
    private final int COLS = 8;

    // Mảng 2 chiều để lưu trạng thái logic và giao diện
    private int[][] board = new int[ROWS][COLS];
    private Button[][] buttons = new Button[ROWS][COLS];
    private final int NUM_MINES = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);

        // Gọi 3 hàm theo đúng thứ tự
        placeMines();
        calculateNumbers();
        initBoardUI();
    }

    private void placeMines() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = 0;
            }
        }

        int minesPlaced = 0;
        while (minesPlaced < NUM_MINES) {
            int r = (int) (Math.random() * ROWS);
            int c = (int) (Math.random() * COLS);

            if (board[r][c] != -1) {
                board[r][c] = -1;
                minesPlaced++;
            }
        }
    }
    private boolean[][] isRevealed = new boolean[ROWS][COLS]; // Lưu trạng thái đã lật hay chưa
    private void calculateNumbers() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == -1) continue;

                int mineCount = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int nr = r + i;
                        int nc = c + j;
                        if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS && board[nr][nc] == -1) {
                            mineCount++;
                        }
                    }
                }
                board[r][c] = mineCount;
            }
        }
    }
    private void revealCell(int r, int c) {
        // Điều kiện dừng: Vượt ra ngoài bản đồ hoặc ô này đã được mở rồi
        if (r < 0 || r >= ROWS || c < 0 || c >= COLS || isRevealed[r][c]) {
            return;
        }

        // Đánh dấu là đã mở
        isRevealed[r][c] = true;
        Button btn = buttons[r][c];

        // Nếu là mìn thì hiện bom (Thua)
        if (board[r][c] == -1) {
            btn.setText("💣");
            btn.setBackgroundColor(0xFFFFCCCC);
            return;
        }

        // Nếu là số > 0 thì hiện số và DỪNG LOANG
        if (board[r][c] > 0) {
            btn.setText(String.valueOf(board[r][c]));
            btn.setBackgroundColor(0xFFEEEEEE);
            return;
        }

        // Nếu là ô trống (số 0): Hiện nền xám và TIẾP TỤC LOANG ra 8 ô xung quanh
        btn.setText("");
        btn.setBackgroundColor(0xFFCCCCCC);

        // Gọi đệ quy cho 8 ô lân cận
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    revealCell(r + i, c + j);
                }
            }
        }
    }
    private void initBoardUI() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Button btn = new Button(this);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 120;
                params.height = 120;
                params.setMargins(2, 2, 2, 2);
                btn.setLayoutParams(params);
                btn.setBackgroundColor(0xFFDDDDDD);

                final int r = i;
                final int c = j;

                btn.setOnClickListener(v -> {
                    if (board[r][c] == -1) {
                        btn.setText("💣");
                        btn.setBackgroundColor(0xFFFFCCCC); // Nền đỏ
                    } else if (board[r][c] > 0) {
                        btn.setText(String.valueOf(board[r][c]));
                        btn.setBackgroundColor(0xFFEEEEEE); // Nền trắng xám
                    } else {
                        btn.setText(""); // Ô trống
                        btn.setBackgroundColor(0xFFCCCCCC); // Nền xám đậm hơn
                    }
                });

                buttons[i][j] = btn;
                gridLayout.addView(btn);
            }
        }
    }
}
