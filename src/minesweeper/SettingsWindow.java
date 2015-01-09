package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;

public class SettingsWindow extends JFrame
{
	private int sWidth;
	private int sHeight;
	private int sScaling;
	private int sBombCount;
	private int sDifficulty;		//0 = custom, 1=easy, 2=medium, 3=hard
	private int sPxLeft;
	private int sPxTop;
	private boolean isVisible;
	
	private SettingsPanel sp;
	
	public static void main(String[] args)
	{
		SettingsWindow w = new SettingsWindow(16,100,30,99,200,200);
	}
	
	public SettingsWindow(int width, int height, int scaling, int bombCount, int left, int top)
	{
		this.sWidth = width;
		this.sHeight = height;
		this.sScaling = scaling;
		this.sBombCount = bombCount;
		this.sPxLeft = left;
		this.sPxTop = top;
		this.sDifficulty = 0;
		this.isVisible = false;
		this.initWindow();
	}
	
	public SettingsWindow(int difficulty)
	{
		this.setSDifficulty(difficulty);
		this.isVisible = false;
		this.initWindow();
	}
	
	private void initWindow()
	{
		this.setTitle("Settings");
		this.setLocation(new Point(100,100));
		this.setResizable(false);
		sp = new SettingsPanel();
		sp.setPreferredSize(new Dimension(200,200));
		this.setPreferredSize(sp.getPreferredSize());
		this.setContentPane(sp);
		this.pack();
		this.setVisible(false);
	}

	public int getSWidth()
	{
		return sWidth;
	}

	public void setSSize(int width, int height)
	{
		this.sWidth = width;
		this.sHeight = height;
	}

	public int getSHeight()
	{
		return sHeight;
	}

	public int getSScaling()
	{
		return sScaling;
	}

	public void setSScaling(int scaling)
	{
		this.sScaling = scaling;
	}

	public int getSBombCount()
	{
		return sBombCount;
	}

	public void setSBombCount(int bombCount)
	{
		this.sBombCount = bombCount;
	}

	public int getSDifficulty()
	{
		return sDifficulty;
	}

	public void setSDifficulty(int difficulty)
	{
		if(difficulty == 0)
		{
			System.out.println("Simply change one value, the difficulty will automatically change");
			return;
		}
		else if(difficulty == 1)		//easy
		{
			this.sWidth = 9;
			this.sHeight = 9;
			this.sScaling = 50;
			this.sBombCount = 10;
			this.sPxLeft = 200;
			this.sPxTop = 200;
		}
		else if(difficulty == 2)		//medium
		{
			this.sWidth = 16;
			this.sHeight = 16;
			this.sScaling = 40;
			this.sBombCount = 40;
			this.sPxLeft = 200;
			this.sPxTop = 200;
		}
		else if(difficulty == 3)		//hard
		{
			this.sWidth = 30;
			this.sHeight = 16;
			this.sScaling = 30;
			this.sBombCount = 99;
			this.sPxLeft = 200;
			this.sPxTop = 200;
		}
		else
		{
			System.out.println("Invalid difficulty was entered!");
			return;
		}
		
		this.sDifficulty = difficulty;
	}

	public int getSPxLeft()
	{
		return sPxLeft;
	}

	public int getSPxTop()
	{
		return sPxTop;
	}
	
	public void setSPxPos(int pxLeft, int pxTop)
	{
		this.sPxLeft = pxLeft;
		this.sPxTop = pxTop;
	}
	
	public void setVisible(boolean isVisible)
	{
		this.isVisible = isVisible;
		super.setVisible(isVisible);
	}
}