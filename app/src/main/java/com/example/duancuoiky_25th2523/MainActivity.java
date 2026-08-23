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
    private boolean[][] isFlagged = new boolean[ROWS][COLS]; // Lưu trạng thái cắm cờ
    private boolean isGameOver = false; // Kiểm tra game đã kết thúc chưa
    private int revealedSafeCells = 0;  // Đếm số ô an toàn đã mở để xét điều kiện thắng

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
        // Điều kiện dừng mới: Game over, hoặc ô đó đã cắm cờ, đã lật, hoặc tràn viền
        if (isGameOver || r < 0 || r >= ROWS || c < 0 || c >= COLS || isRevealed[r][c] || isFlagged[r][c]) {
            return;
        }

        isRevealed[r][c] = true;
        Button btn = buttons[r][c];

        // TRƯỜNG HỢP 1: ĐẠP TRÚNG MÌN -> THUA CUỘC
        if (board[r][c] == -1) {
            btn.setText("💣");
            btn.setBackgroundColor(0xFFFFCCCC); // Nền đỏ
            isGameOver = true;

            // Đổi dòng chữ trạng thái trên cùng
            android.widget.TextView tvStatus = findViewById(R.id.tvStatus);
            tvStatus.setText("Trạng thái: BẠN ĐÃ THUA! 💥");
            return;
        }

        // TRƯỜNG HỢP 2: Ô AN TOÀN
        revealedSafeCells++; // Tăng biến đếm ô an toàn lên 1

        if (board[r][c] > 0) {
            btn.setText(String.valueOf(board[r][c]));
            btn.setBackgroundColor(0xFFEEEEEE);
        } else {
            btn.setText("");
            btn.setBackgroundColor(0xFFCCCCCC);
            // Loang ra 8 ô xung quanh
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        revealCell(r + i, c + j);
                    }
                }
            }
        }

        // KIỂM TRA ĐIỀU KIỆN THẮNG: Đã mở hết các ô không phải mìn
        if (revealedSafeCells == (ROWS * COLS) - NUM_MINES) {
            isGameOver = true;
            android.widget.TextView tvStatus = findViewById(R.id.tvStatus);
            tvStatus.setText("Trạng thái: BẠN ĐÃ CHIẾN THẮNG! 🏆");
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

                // Sự kiện click bình thường (đã có từ trước)
                btn.setOnClickListener(v -> {
                    revealCell(r, c);
                });

                // THÊM MỚI: Sự kiện nhấn giữ để cắm cờ 🚩
                btn.setOnLongClickListener(v -> {
                    // Nếu game đã kết thúc hoặc ô đã bị lật thì bỏ qua
                    if (isGameOver || isRevealed[r][c]) return true;

                    if (isFlagged[r][c]) {
                        // Nếu đã cắm cờ rồi -> Rút cờ ra
                        isFlagged[r][c] = false;
                        btn.setText("");
                    } else {
                        // Nếu chưa cắm cờ -> Cắm cờ vào
                        isFlagged[r][c] = true;
                        btn.setText("🚩");
                    }
                    return true; // Trả về true để hệ thống biết đã xử lý xong Long Click
                });

                buttons[i][j] = btn;
                gridLayout.addView(btn);
            }
        }
    }
}
