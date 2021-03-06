import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.Timer;

public class Logica implements ILogica {
    Jogo jogo;

    public boolean jogando;

    public List<Player> players = null;
    public Enemy[][] enemies = new Enemy[17][13];
    public int enemyCount = 0;

    Logica(IJogo jogo) {
        this.jogo = (Jogo) jogo;
    }

    public void executa() {
        AtomicBoolean roda = new AtomicBoolean(true);

        this.players = new ArrayList<>();
        this.players.add(new Player(0, 3));
        this.players.add(new Player(16, 3));
        this.jogo.sendCommand(0, "READY", new String[] { "P1", "" + this.players.get(0).x, "" + this.players.get(0).y,
                "" + this.players.get(1).x, "" + this.players.get(1).y, "" + this.jogo.segundos });
        this.jogo.sendCommand(1, "READY", new String[] { "P2", "" + this.players.get(1).x, "" + this.players.get(1).y,
                "" + this.players.get(0).x, "" + this.players.get(0).y, "" + this.jogo.segundos });

        Timer en = new Timer(2000, (ae) -> {
            if (!roda.get()) {
                ((Timer) ae.getSource()).stop();
                return;
            }

            this.doEnemiesHit();
        });

        Timer jogo = new Timer(1000, (ae) -> {
            if (!roda.get()) {
                ((Timer) ae.getSource()).stop();
                return;
            }

            if (this.jogo.segundos > 0) {
                this.jogo.segundos--;
            } else {
                roda.set(false);
                ((Timer) ae.getSource()).stop();
            }
        });

        Timer t = new Timer(10000, (ae) -> {
            if (!roda.get()) {
                ((Timer) ae.getSource()).stop();
                return;
            }

            Enemy enemy = this.spawnEnemy();
            if (enemy == null) {
                return;
            }

            enemies[enemy.x][enemy.y] = enemy;

            this.jogo.sendCommand(0, "SPAWNENEMY", new String[] { "" + enemy.x, "" + enemy.y });
            this.jogo.sendCommand(1, "SPAWNENEMY", new String[] { "" + enemy.x, "" + enemy.y });
        });

        en.start();
        t.start();
        jogo.start();

        new Thread(() -> {
            while (this.jogo.clienteVivo[0] && this.jogo.clienteVivo[1] && this.jogo.segundos > 0 && roda.get()) {
                //
            }

            System.out.println("Fim de jogo");

            roda.set(false);

            int P1pontos = this.players.get(0).score;
            int P2pontos = this.players.get(1).score;

            if (this.jogo.clienteVivo[0] && this.jogo.clienteVivo[1]) {
                if (P1pontos == P2pontos) {
                    this.jogo.sendCommand(0, "END", new String[] { "D" });
                    this.jogo.sendCommand(1, "END", new String[] { "D" });
                } else {
                    this.jogo.sendCommand(0, "END", new String[] { P1pontos > P2pontos ? "P1" : "P2" });
                    this.jogo.sendCommand(1, "END", new String[] { P1pontos > P2pontos ? "P2" : "P1" });
                }
            } else if (this.jogo.clienteVivo[0]) {
                this.jogo.sendCommand(0, "END", new String[] { "P1" });
                this.jogo.sendCommand(1, "END", new String[] { "P2" });
            } else {
                this.jogo.sendCommand(0, "END", new String[] { "P2" });
                this.jogo.sendCommand(1, "END", new String[] { "P1" });
            }
        }).start();
    }

    public int getDamage() {
        return (int) Math.ceil(5 + Math.random() * (10 - 5));
    }

    public boolean colide(int player, int x, int y) {
        return x < 0 || x > 16 || y < 3 || y > 12
                || (x == this.players.get(1 - player).x && y == this.players.get(1 - player).y)
                || this.enemies[x][y] != null;
    }

    public boolean movePlayer(int player, int dx, int dy) {
        Player p = this.players.get(player);
        p.x += dx;
        p.y += dy;

        if (dx == 0 && dy < 0) {
            p.direction = "up";
        } else if (dx == 0 && dy > 0) {
            p.direction = "down";
        } else if (dx < 0 && dy == 0) {
            p.direction = "left";
        } else if (dx > 0 && dy == 0) {
            p.direction = "right";
        }

        if (this.colide(player, p.x, p.y)) {
            p.x -= dx;
            p.y -= dy;
        }

        return true;
    }

    public boolean hitEnemy(int numJogador, int x, int y, int damage) {
        if (x < 0 || x > 16 || y < 3 || y > 12 || this.enemies[x][y] == null) {
            return false;
        }

        this.enemies[x][y].life -= damage;

        if (this.enemies[x][y].life <= 0) {
            this.enemies[x][y] = null;
            this.enemyCount--;

            this.players.get(numJogador).score += 10;

            if (numJogador == 0) {
                this.jogo.sendCommand(0, "ENEMYKILL", new String[] { "P1" });
                this.jogo.sendCommand(1, "ENEMYKILL", new String[] { "P2" });
            } else {
                this.jogo.sendCommand(0, "ENEMYKILL", new String[] { "P2" });
                this.jogo.sendCommand(1, "ENEMYKILL", new String[] { "P1" });
            }
        }

        return true;
    }

    public Enemy spawnEnemy() {
        if (this.enemyCount == 17 * 10 - 3) {
            return null;
        }

        int x = 0;
        int y = 0;
        do {
            x = (int) Math.ceil(0 + Math.random() * (16 - 0));
            y = (int) Math.ceil(3 + Math.random() * (12 - 3));
        } while (this.enemies[x][y] != null || (this.players.get(0).x == x && this.players.get(0).y == y)
                || (this.players.get(1).x == x && this.players.get(1).y == y));

        this.enemyCount++;

        return new Enemy(x, y);
    }

    public void doEnemiesHit() {
        int P1x = this.players.get(0).x;
        int P1y = this.players.get(0).y;

        if (this.jogo.clienteVivo[0]) {
            for (int x = P1x - 1; x <= P1x + 1; x++) {
                for (int y = P1y - 1; y <= P1y + 1; y++) {
                    if (x > 0 && x < 17 && y > 2 && y < 13 && this.enemies[x][y] != null) {
                        int damage = this.getDamage();
                        this.players.get(0).life -= damage;

                        if (this.players.get(0).life < 0) {
                            this.jogo.clienteVivo[0] = false;
                        }

                        this.jogo.sendCommand(0, "ENEMYHIT", new String[] { "P1", "" + damage, "" + x, "" + y });
                        this.jogo.sendCommand(1, "ENEMYHIT", new String[] { "P2", "" + damage, "" + x, "" + y });
                        break;
                    }
                }
            }
        }

        int P2x = this.players.get(1).x;
        int P2y = this.players.get(1).y;

        if (this.jogo.clienteVivo[1]) {
            for (int x = P2x - 1; x <= P2x + 1; x++) {
                for (int y = P2y - 1; y <= P2y + 1; y++) {
                    if (x > 0 && x < 17 && y > 2 && y < 13 && this.enemies[x][y] != null) {
                        int damage = this.getDamage();
                        this.players.get(0).life -= damage;

                        if (this.players.get(0).life < 0) {
                            this.jogo.clienteVivo[0] = false;
                        }

                        this.jogo.sendCommand(0, "ENEMYHIT", new String[] { "P2", "" + damage, "" + x, "" + y });
                        this.jogo.sendCommand(1, "ENEMYHIT", new String[] { "P1", "" + damage, "" + x, "" + y });
                        break;
                    }
                }
            }
        }
    }
}
