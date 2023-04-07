package rttt;


import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Display extends JPanel {
    private TicTacToe game;
    private Color oColor;
    private Color xColor;
    private int selectedGame;
    private boolean mouseIsPressed;
    private Point mousePosition;
    private List<Animation> currentAnimations;

    private MouseActions mouseActions;
    private KeyActions keyActions;

    private Rectangle backTrackButton;

    public Display(TicTacToe game) {
        this.game = game;
        oColor = Color.BLUE;
        xColor = Color.RED;
        selectedGame = -1;
        setBackground(Color.LIGHT_GRAY);
        mousePosition = new Point(0, 0);

        mouseActions = new MouseActions();
        keyActions = new KeyActions();
        addMouseListener(mouseActions);
        addMouseMotionListener(mouseActions);
        addKeyListener(keyActions);
        backTrackButton = new Rectangle(0, 0, 0, 0);
        currentAnimations = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(Resources.getImage(Resources.ImageEnum.SUN_BACKGROUND), 0, 0, getWidth(), getHeight(), null);
        if (game.getWinner() != Player.NONE) {
            renderWin(g2D);
        } else {


            renderHoveredGameChoice(g2D);
            renderGame(g2D, game.getCurrentGame(), new Rectangle(0, 0, getWidth(), getHeight()), 0);
            drawCurrentPlayerDisplay(g2D);
            drawCurrentDepthDisplay(g2D);

            if (game.canBacktrack()) {
                drawBackTrackArea(g2D);
            } else {
                backTrackButton.setBounds(0, 0, 0, 0);
            }

            for (Animation animation : currentAnimations) {
                animation.animate(g2D);
            }

        }
    }

    private void renderWin(Graphics2D g2D) {
        if (game.getWinner() == Player.X) {
            drawX(g2D, new Rectangle(0, 0, getWidth(), getHeight()));
        } else if (game.getWinner() == Player.O) {
            drawO(g2D, new Rectangle(0, 0, getWidth(), getHeight()));
        }

        FontMetrics metrics = g2D.getFontMetrics();
        String winPrompt = "Player " + game.getWinner() + " HAS WON";
        g2D.drawString(winPrompt, getWidth() / 2 - metrics.stringWidth(winPrompt) / 2, metrics.getHeight());
    }

    private void renderGame(Graphics2D g2D, Game currentGame, Rectangle gameDimensions, int depth) {
        if (currentGame == null) {
            System.out.println("[Display.renderGame()] FATAL: HIT NULL GAME");
            return;
        } else if (gameDimensions.width < 5 || gameDimensions.height < 5) {
            System.out.println("[Display.renderGame()] Reached maximum render depth");
            return;
        }

        if (currentGame.isFinished()) {
            if (currentGame.getWinner() == Player.X) {
                drawX(g2D, gameDimensions);
            } else if (currentGame.getWinner() == Player.O) {
                drawO(g2D, gameDimensions);
            }
            return;
        }

        Dimension childGameDimensions = new Dimension(gameDimensions.width / 3, gameDimensions.height / 3);
        if (currentGame instanceof LeafGame) {
            for (int i = 0; i < 9; i++) {
                int currentRow = i / 3;
                int currentCol = i % 3;
                Point movePos = new Point(gameDimensions.x + (currentCol * childGameDimensions.width), gameDimensions.y + (currentRow * childGameDimensions.height));
                if (( (LeafGame) currentGame).getPlayerAt(currentRow, currentCol) == Player.X) {
                    drawX(g2D, new Rectangle(movePos.x, movePos.y, childGameDimensions.width, childGameDimensions.height));
                } else if (( (LeafGame) currentGame).getPlayerAt(currentRow, currentCol) == Player.O) {
                    drawO(g2D, new Rectangle(movePos.x, movePos.y, childGameDimensions.width, childGameDimensions.height));
                }
            }
        } else if (currentGame instanceof InteriorGame) {
            for (int i = 0; i < 9; i++) {
                int currentRow = i / 3;
                int currentCol = i % 3;
                Point gamePos = new Point(gameDimensions.x + (currentCol * childGameDimensions.width), gameDimensions.y + (currentRow * childGameDimensions.height));
                renderGame(g2D, ( (InteriorGame) currentGame).getGameAt(currentRow, currentCol), new Rectangle(gamePos.x, gamePos.y, childGameDimensions.width, childGameDimensions.height), depth + 1);
            }
        }

        drawGrid(g2D, gameDimensions, depth);
    }

    private void drawX(Graphics2D g2D, Rectangle bounds) {
        Stroke originalStroke = g2D.getStroke();
        Paint originalPaint = g2D.getPaint();
        Composite originalComposite = g2D.getComposite();
        int strokeSize = Math.min(bounds.width / 50, bounds.height / 50);
        int xPadding = bounds.width / 10;
        int yPadding = bounds.height / 10;
        g2D.setStroke(new BasicStroke(strokeSize));
        g2D.setPaint(xColor);

        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2D.fill(bounds);
        g2D.setComposite(originalComposite);

        g2D.drawLine(bounds.x + xPadding, bounds.y + yPadding, bounds.x + bounds.width - xPadding, bounds.y + bounds.height - yPadding);
        g2D.drawLine(bounds.x + bounds.width - xPadding, bounds.y + yPadding, bounds.x + xPadding, bounds.y + bounds.height - yPadding);

        g2D.setStroke(originalStroke);
        g2D.setPaint(originalPaint);
    }

    private void drawO(Graphics2D g2D, Rectangle bounds) {
        Stroke originalStroke = g2D.getStroke();
        Paint originalPaint = g2D.getPaint();
        Composite originalComposite = g2D.getComposite();
        int strokeSize = Math.min(bounds.width / 50, bounds.height / 50);
        int xPadding = bounds.width / 10;
        int yPadding = bounds.height / 10;
        g2D.setStroke(new BasicStroke(strokeSize));
        g2D.setPaint(oColor);

        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2D.fill(bounds);
        g2D.setComposite(originalComposite);

        g2D.drawOval(bounds.x + xPadding, bounds.y + yPadding, bounds.width - xPadding * 2, bounds.height - yPadding * 2);

        g2D.setStroke(originalStroke);
        g2D.setPaint(originalPaint);
    }

    private void drawGrid(Graphics2D g2D, Rectangle bounds, int depth) {
        Stroke originalStroke = g2D.getStroke();
        Paint originalPaint = g2D.getPaint();
        int strokeSize = Math.min(Math.min(bounds.width / 30, bounds.height / 30), 10);
        g2D.setStroke(new BasicStroke(strokeSize));
        Color gridColor = depth % 2 == 0 ? Color.BLACK : new Color(60, 60, 60);
        g2D.setPaint(gridColor);
        int gridCellWidth = bounds.width / 3;
        int gridCellHeight = bounds.height / 3;

        for (int i = 1; i <= 2; i++) {
            g2D.drawLine(bounds.x + gridCellWidth * i, bounds.y, bounds.x + gridCellWidth * i, bounds.y + bounds.height);
            g2D.drawLine(bounds.x, bounds.y + gridCellHeight * i, bounds.x + bounds.width, bounds.y + gridCellHeight * i);
        }

        g2D.setStroke(originalStroke);
        g2D.setPaint(originalPaint);
    }

    private void renderHoveredGameChoice(Graphics2D g2D) {
        if (selectedGame < 0)
            return;
        Paint originalPaint = g2D.getPaint();
        Color highlightColor;
        if (mouseIsPressed) {
            highlightColor = new Color(30, 30, 30);
        } else {
            highlightColor = new Color(40, 40, 40);
        }

        g2D.setPaint(highlightColor);
        Rectangle choiceBounds = new Rectangle( selectedGame % 3 * (getWidth() / 3), selectedGame / 3 * (getHeight() / 3), getWidth() / 3, getHeight() / 3);
        g2D.fill(choiceBounds);
        g2D.setPaint(originalPaint);
    }

    private void drawCurrentPlayerDisplay(Graphics2D g2D) {
        Stroke originalStroke = g2D.getStroke();
        Paint originalPaint = g2D.getPaint();
        Composite originalComposite = g2D.getComposite();

        Dimension currentPlayerDisplayDimension = new Dimension(getWidth() / 10, getHeight() / 10);
        int xMargin = 10;
        int yMargin = 10;
        Rectangle currentPlayerDisplay = new Rectangle(getWidth() - currentPlayerDisplayDimension.width - xMargin, getHeight() - currentPlayerDisplayDimension.height - yMargin, currentPlayerDisplayDimension.width, currentPlayerDisplayDimension.height);

        if (currentPlayerDisplay.contains(mousePosition)) {
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }

        g2D.setPaint(Color.WHITE);
        g2D.fill(currentPlayerDisplay);
        g2D.setPaint(Color.BLACK);
        g2D.setStroke(new BasicStroke(5));
        g2D.draw(currentPlayerDisplay);

        g2D.setStroke(originalStroke);
        g2D.setPaint(originalPaint);
        g2D.setComposite(originalComposite);

        if (game.getCurrentPlayer() == Player.X) {
            drawX(g2D, currentPlayerDisplay);
        } else if (game.getCurrentPlayer() == Player.O) {
            drawO(g2D, currentPlayerDisplay);
        }
    }

    private void drawBackTrackArea(Graphics2D g2D) {
        Stroke originalStroke = g2D.getStroke();
        Paint originalPaint = g2D.getPaint();
        Composite originalComposite = g2D.getComposite();
        Font originalFont = g2D.getFont();

        Dimension backTrackButtonDimension = new Dimension(getWidth() / 10, getHeight() / 10);
        int xMargin = 10;
        int yMargin = 10;
        backTrackButton.setBounds(xMargin, getHeight() - backTrackButtonDimension.height - yMargin, backTrackButtonDimension.width, backTrackButtonDimension.height);

        if (backTrackButton.contains(mousePosition)) {
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }

        g2D.setPaint(Color.WHITE);
        g2D.fill(backTrackButton);
        g2D.setPaint(Color.BLACK);
        g2D.setStroke(new BasicStroke(5));
        g2D.draw(backTrackButton);

        FontMetrics metrics = g2D.getFontMetrics();
        String prompt = "Back <";

        while (metrics.stringWidth(prompt) < backTrackButton.width) {
            g2D.setFont(g2D.getFont().deriveFont( g2D.getFont().getSize() + 1f ));
            metrics = g2D.getFontMetrics();
        }
        g2D.setFont(g2D.getFont().deriveFont( g2D.getFont().getSize() - 1f ));
        metrics = g2D.getFontMetrics();
        g2D.drawString(prompt, backTrackButton.x + backTrackButton.width / 2 - metrics.stringWidth(prompt) / 2, backTrackButton.y + backTrackButton.height / 2 + metrics.getHeight() / 2);

        g2D.setFont(originalFont);
        g2D.setStroke(originalStroke);
        g2D.setPaint(originalPaint);
        g2D.setComposite(originalComposite);
    }

    private void drawCurrentDepthDisplay(Graphics2D g2D) {
        Stroke originalStroke = g2D.getStroke();
        Paint originalPaint = g2D.getPaint();
        Composite originalComposite = g2D.getComposite();
        Font originalFont = g2D.getFont();

        Dimension currentDepthDisplayDimension = new Dimension(getWidth() / 10, getHeight() / 10);
        int yMargin = 10;
        Rectangle currentDepthDisplay = new Rectangle(getWidth() / 2 - currentDepthDisplayDimension.width / 2, getHeight() - currentDepthDisplayDimension.height - yMargin, currentDepthDisplayDimension.width, currentDepthDisplayDimension.height);

        if (currentDepthDisplay.contains(mousePosition)) {
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }

        g2D.setPaint(Color.WHITE);
        g2D.fill(currentDepthDisplay);
        g2D.setPaint(Color.BLACK);
        g2D.setStroke(new BasicStroke(5));
        g2D.draw(currentDepthDisplay);

        FontMetrics metrics = g2D.getFontMetrics();
        String prompt = "" + game.getCurrentGame().getDepth();

        while (metrics.stringWidth(prompt) < currentDepthDisplay.width && metrics.getHeight() < currentDepthDisplay.height) {
            g2D.setFont(g2D.getFont().deriveFont( g2D.getFont().getSize() + 1f ));
            metrics = g2D.getFontMetrics();
        }
        g2D.setFont(g2D.getFont().deriveFont( g2D.getFont().getSize() - 1f ));
        metrics = g2D.getFontMetrics();
        g2D.drawString(prompt, currentDepthDisplay.x + currentDepthDisplay.width / 2 - metrics.stringWidth(prompt) / 2, currentDepthDisplay.y + currentDepthDisplay.height / 2 + metrics.getHeight() / 4);

        g2D.setFont(originalFont);
        g2D.setStroke(originalStroke);
        g2D.setPaint(originalPaint);
        g2D.setComposite(originalComposite);
    }

    private void lockInputs() {
        removeKeyListener(keyActions);
        removeMouseListener(mouseActions);
        removeMouseMotionListener(mouseActions);
    }

    private void unlockInputs() {
        if (!Arrays.stream(getKeyListeners()).anyMatch(l -> l.equals(keyActions))) {
            addKeyListener(keyActions);
        }
        if (!Arrays.stream(getMouseListeners()).anyMatch(l -> l.equals(mouseActions))) {
            addMouseListener(mouseActions);
        }
        if (!Arrays.stream(getMouseMotionListeners()).anyMatch(l -> l.equals(mouseActions))) {
            addMouseMotionListener(mouseActions);
        }
    }

    private void startDrawXAnimation(int row, int col) {
        final Rectangle bounds = getBoundsFromRowAndCol(row, col);

        lockInputs();
        final DrawXAnimation animation = new DrawXAnimation(bounds);
        final Runnable callAnimate = () -> SwingUtilities.invokeLater( () -> repaint() );
        final Runnable endAnimation = () -> SwingUtilities.invokeLater( () -> {
            game.chooseSelection(row, col);
            currentAnimations.remove(animation);
            repaint();
            unlockInputs();
        } );
        final Scheduler.Preferences<Runnable> animationInfo = new Scheduler.Preferences<Runnable>();
        animationInfo.setCancelTime(animation.getTransitionTime());
        animationInfo.setLoopTime(animation.getMillisecondsPerFrame());
        animationInfo.setMainAction(callAnimate);
        animationInfo.setTimeUnit(TimeUnit.MILLISECONDS);
        animationInfo.setOnCancel(endAnimation);
        currentAnimations.add(animation);

        Scheduler.scheduleAtFixedRate(animationInfo);
    }

    private Rectangle getBoundsFromRowAndCol(int row, int col) {
        int squareWidth = getWidth() / 3;
        int squareHeight = getHeight() / 3;
        return new Rectangle(col * squareWidth, row * squareHeight, squareWidth, squareHeight);
    }

    class MouseActions extends MouseAdapter {
        boolean clickActionTaken;
        MouseActions() {}

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (!clickActionTaken) {
                int selectedRow = selectedGame / 3;
                int selectedCol = selectedGame % 3;
//                game.chooseSelection(selectedRow, selectedCol);
                if (game.getCurrentPlayer() == Player.X) {
                    startDrawXAnimation(selectedRow, selectedCol);
                } else {
                    game.chooseSelection(selectedRow, selectedCol);
                }
                repaint();
            }
            mouseIsPressed = false;
            clickActionTaken = false;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            mouseIsPressed = true;
            if (game.canBacktrack() && backTrackButton.contains(e.getPoint())) {
                game.backtrack();
                clickActionTaken = true;
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            mousePosition.setLocation(e.getX(), e.getY());
            Dimension gameDimension = new Dimension(getWidth() / 3, getHeight() / 3);
            if (selectedGame >= 0 && selectedGame < 9)
                repaint(selectedGame % 3 * gameDimension.width, selectedGame / 3 * gameDimension.height, gameDimension.width, gameDimension.height);
            selectedGame = getSelectedArea(e.getPoint());
            repaint(selectedGame % 3 * gameDimension.width, selectedGame / 3 * gameDimension.height, gameDimension.width, gameDimension.height);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            mouseMoved(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            selectedGame = -1;
            repaint();
        }

        private int getSelectedArea(Point point) {
            return (point.x / (getWidth() / 3)) + (point.y / (getHeight() / 3) * 3);
        }
    }

    class KeyActions extends KeyAdapter {
        KeyActions() {}
    }

    private abstract class Animation {
        private int maxFrames;
        private int transitionTime;
        private Instant lastFrameInstant;

        Animation(int maxFrames, int transitionTime) {
            this.maxFrames = maxFrames;
            this.transitionTime = transitionTime;
            lastFrameInstant = Instant.now();
        }

        public void animate(Graphics2D g2D) {
            if (canUpdate()) {
                for (int i = 0; i < getFramesSinceLastFrame(); i++) {
                    update();
                }
                lastFrameInstant = Instant.now();
            }
            draw(g2D);
        }

        public int getTransitionTime() {
            return transitionTime;
        }

        public int getMillisecondsPerFrame() {
            return transitionTime / maxFrames;
        }

        protected int getMaxFrames() {
            return maxFrames;
        }

        protected boolean canUpdate() {
            if (Duration.between(lastFrameInstant, Instant.now()).toMillisPart() >= getMillisecondsPerFrame())
                return true;
            return false;
        }

        private int getFramesSinceLastFrame() {
            return Duration.between(lastFrameInstant, Instant.now()).toMillisPart() / getMillisecondsPerFrame();
        }

        public abstract void update();
        public abstract void draw(Graphics2D g2D);
    }

    private class DrawXAnimation extends Animation {

        Rectangle drawingBounds;
        private int desiredLineWidth, desiredLineHeight,
                currentLineWidth, currentLineHeight,
                xPadding, yPadding, strokeSize;

        DrawXAnimation(Rectangle bounds) {
            super(10, 150);
            this.drawingBounds = bounds;
            currentLineWidth = 0;
            currentLineHeight = 0;
            strokeSize = Math.min(drawingBounds.width / 50, drawingBounds.height / 50);
            xPadding = drawingBounds.width / 10;
            yPadding = drawingBounds.height / 10;
            desiredLineWidth = bounds.width - (xPadding * 2);
            desiredLineHeight = bounds.height - (yPadding * 2);
        }

        @Override
        public void update() {
            currentLineWidth += desiredLineWidth / getMaxFrames();
            currentLineHeight += desiredLineHeight / getMaxFrames();
        }

        @Override
        public void draw(Graphics2D g2D) {
            Stroke originalStroke = g2D.getStroke();
            Paint originalPaint = g2D.getPaint();
            Composite originalComposite = g2D.getComposite();
            g2D.setStroke(new BasicStroke(strokeSize));
            g2D.setPaint(xColor);

            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2D.fill(new Rectangle(drawingBounds.x, drawingBounds.y, drawingBounds.width, currentLineHeight + yPadding));
            g2D.setComposite(originalComposite);

            g2D.drawLine(drawingBounds.x + xPadding, drawingBounds.y + yPadding, drawingBounds.x + xPadding + currentLineWidth, drawingBounds.y + yPadding + currentLineHeight);
            g2D.drawLine(drawingBounds.x + drawingBounds.width - xPadding, drawingBounds.y + yPadding, drawingBounds.x + drawingBounds.width - xPadding - currentLineWidth, drawingBounds.y + yPadding + currentLineHeight);

            g2D.setStroke(originalStroke);
            g2D.setPaint(originalPaint);
        }
    }

//    private class DrawOAnimation extends Animation {
//
//    }
//
//    private class ZoomInAnimation extends Animation {
//
//    }
//
//    private class ZoomOutAnimation extends Animation {
//
//    }

}
