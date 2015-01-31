package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class PlayingField extends JPanel implements MouseListener
{
	private Field[][] fields;			//0=not opened ; 1=opened ; 2=flag
	private Settings settings;
	private MainWindow mainWindow;
	private int fieldsToOpen;
	
	private BufferedImage buffer;
	
	public PlayingField(MainWindow mw)
	{
		this.mainWindow = mw;
		this.settings = mainWindow.getSettings();
		
		int w = settings.getWidth();
		int h = settings.getHeight();
		
		this.fields = new Field[w][h];
		
		this.initFields();
		this.placeBombs();
		this.setSize(new Dimension(w*settings.getScaling(),h*settings.getScaling()));
		
		buffer = new BufferedImage(w*settings.getScaling(),h*settings.getScaling(),BufferedImage.TYPE_INT_RGB);
		
		this.fieldsToOpen = (settings.getWidth()*settings.getHeight())-settings.getBombCount();
		
		addMouseListener(this);
	}
	
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics bufferGraphics = buffer.getGraphics();
		
		int w = settings.getWidth();
		int h = settings.getHeight();
		int s = settings.getScaling();
		
		for(int top = 0; top < h; top++)
		{
			for(int left = 0; left < w; left++)
			{
				if(fields[left][top].getFieldStatus() == Field.NOT_OPENED)
				{
					bufferGraphics.setColor(Color.DARK_GRAY);
					bufferGraphics.fillRect(left*s, top*s, s, s);
				}
				else if(fields[left][top].getFieldStatus() == Field.OPENED)
				{
					bufferGraphics.setColor(Color.GRAY);
					bufferGraphics.fillRect(left*s, top*s, s, s);
					
					bufferGraphics.setColor(Color.BLACK);
					if(fields[left][top].getBombStatus() == Field.BOMBED)
						bufferGraphics.drawString("x", left*s + s/2, top*s + s/2);
					else if(fields[left][top].getBombStatus() > 0)
						bufferGraphics.drawString(fields[left][top].getBombStatus() + "", left*s + s/2, top*s + s/2);
				}
				else if(fields[left][top].getFieldStatus() == Field.FLAGGED)
				{
					bufferGraphics.setColor(Color.RED);
					bufferGraphics.fillRect(left*s, top*s, s, s);
				}
			}
		}
		
		bufferGraphics.setColor(Color.BLACK);
		for(int i = 0; i <= w; i++)
			bufferGraphics.drawLine(i*s, 0, i*s, h*s);
		
		for(int i = 0; i <= h; i++)
			bufferGraphics.drawLine(0, i*s, w*s, i*s);
		
		if(g != null)
			g.drawImage(buffer, 0, 0, null);
	}
	
	private void initFields()
	{
		for(int y = 0; y < settings.getHeight(); y++)
			for(int x = 0; x < settings.getWidth(); x++)
				this.fields[x][y] = new Field(x,y,settings,this);
		
		for(int y = 0; y < settings.getHeight(); y++)
		{
			for(int x = 0; x < settings.getWidth(); x++)
			{
				fields[x][y].top = (y > 0) ? fields[x][y-1] : null;
				fields[x][y].left = (x > 0) ? fields[x-1][y] : null;
				fields[x][y].right = (x+1 < settings.getWidth()) ? fields[x+1][y] : null;
				fields[x][y].bottom = (y+1 < settings.getHeight()) ? fields[x][y+1] : null;
			}
		}
	}
	
	private void placeBombs()
	{
		int w = settings.getWidth();
		int h = settings.getHeight();
		
		int x,y;
		for(int i = 0; i < settings.getBombCount(); i++)
		{
			x = (int) (Math.random()*w);
			y = (int) (Math.random()*h);
			
			if(fields[x][y].getBombStatus() != Field.BOMBED)
			{
				fields[x][y].setBombStatus(Field.BOMBED);
				for(Direction d : Direction.values())
					if(fields[x][y].getNeighbour(d) != null)
						if(fields[x][y].getNeighbour(d).getBombStatus() != Field.BOMBED)
							fields[x][y].getNeighbour(d).incBombs();
			}
		}
	}
	
	public void gameOver()
	{
		for(int top = 0; top < settings.getHeight(); top++)
			for(int left = 0; left < settings.getWidth(); left++)
				this.fields[left][top].setFieldStatus(Field.OPENED);
		
		paintComponent(this.getGraphics());
		
		JOptionPane.showMessageDialog(null, "Sie haben verloren!", "Schade", JOptionPane.OK_CANCEL_OPTION);
		mainWindow.newGame();
	}
	
	public void youWin()
	{
		for(int top = 0; top < settings.getHeight(); top++)
			for(int left = 0; left < settings.getWidth(); left++)
				this.fields[left][top].setFieldStatus(Field.OPENED);
		
		paintComponent(this.getGraphics());
		
		JOptionPane.showMessageDialog(null, "Sie haben gewonnen!", "Gl�ckwunsch", JOptionPane.OK_CANCEL_OPTION);
		mainWindow.newGame();
	}
	
	public int getFieldsToOpen()
	{
		return fieldsToOpen;
	}
	
	public void decFieldsToOpen()
	{
		fieldsToOpen--;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(e.getClickCount() == 2)
		{
			int x = e.getX()/settings.getScaling();
			int y = e.getY()/settings.getScaling();
			int markedNeighbours = 0;
			Field f = fields[x][y];
			
			if(f.getBombStatus() > 0 && f.getBombStatus() < Field.BOMBED)
			{
				for(Direction d : Direction.values())
					if(f.getNeighbour(d) != null)
						if(f.getNeighbour(d).getFieldStatus() == Field.FLAGGED)
							markedNeighbours++;
				
				if(markedNeighbours == fields[x][y].getBombStatus())
					for(Direction d : Direction.values())
						if(f.getNeighbour(d) != null)
							if(f.getNeighbour(d).getFieldStatus() == Field.NOT_OPENED)
								f.getNeighbour(d).openField();
			}			
		}
	}


	@Override
	public void mouseEntered(MouseEvent e)
	{
	
	}


	@Override
	public void mouseExited(MouseEvent e)
	{

	}


	@Override
	public void mousePressed(MouseEvent e)
	{

	}


	@Override
	public void mouseReleased(MouseEvent e)
	{
		int mx = e.getX()/settings.getScaling();
		int my = e.getY()/settings.getScaling();
		if(e.getButton() == MouseEvent.BUTTON1 && fields[mx][my].getFieldStatus() != Field.FLAGGED)
		{
			fields[mx][my].openField();
		}
		else if(e.getButton() == MouseEvent.BUTTON3)
		{
			if(fields[mx][my].getFieldStatus() == Field.NOT_OPENED)
				fields[mx][my].setFieldStatus(Field.FLAGGED);
			else if(fields[mx][my].getFieldStatus() == Field.FLAGGED)
				fields[mx][my].setFieldStatus(Field.NOT_OPENED);
			paintComponent(this.getGraphics());
		}
	}
}
