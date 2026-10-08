package com.ni.powerpuzzle;

import android.app.*;
import android.os.*;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.content.*;
import java.util.*;

public class MainActivity extends Activity {
  GameView game;
  @Override public void onCreate(Bundle b){ super.onCreate(b); getWindow().setStatusBarColor(Color.BLACK); game=new GameView(this); setContentView(game); }
}

class GameView extends View {
  Paint p=new Paint(3); Paint text=new Paint(3); int level=0, moves=0; int px,py; final int N=6;
  float downX,downY; boolean won=false;
  String[][] boards={
    {"P.....",".###..","...#..",".###..","......",".....G"},
    {"P.#...","..#.#.","..#.#.","....#.",".###..",".....G"},
    {"P.....",".###..","...#..",".##...","....##",".....G"},
    {"P..#..","##.#..","...#..",".###..","......",".....G"},
    {"P.#...","..#...","..###.","......",".####.",".....G"}
  };
  boolean[][] wall=new boolean[N][N];
  GameView(Context c){ super(c); p.setTypeface(Typeface.DEFAULT); text.setTypeface(Typeface.create("sans",Typeface.BOLD)); reset(); setBackgroundColor(Color.BLACK); }
  void reset(){ String[] b=boards[level]; for(int y=0;y<N;y++)for(int x=0;x<N;x++){char c=b[y].charAt(x); wall[y][x]=c=='#'; if(c=='P'){px=x;py=y;}} moves=0;won=false; invalidate(); }
  protected void onDraw(Canvas c){ super.onDraw(c); float w=getWidth(), h=getHeight();
    p.setColor(Color.WHITE); p.setTextAlign(Paint.Align.CENTER); text.setTextAlign(Paint.Align.CENTER);
    text.setTextSize(Math.max(20,w*.065f)); c.drawText("اراده به قدرت",w/2,48,text);
    text.setTextSize(Math.max(12,w*.034f)); p.setColor(Color.LTGRAY); c.drawText("پازلِ خودْفزونی — مرحله "+(level+1)+" از 5",w/2,76,text);
    float top=105, side=Math.min(w-36,h-220), cell=side/N, left=(w-side)/2;
    for(int y=0;y<N;y++)for(int x=0;x<N;x++){
      p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(1); p.setColor(Color.DKGRAY); c.drawRect(left+x*cell,top+y*cell,left+(x+1)*cell,top+(y+1)*cell,p);
      if(wall[y][x]){p.setStyle(Paint.Style.FILL);p.setColor(Color.WHITE);c.drawRect(left+x*cell+3,top+y*cell+3,left+(x+1)*cell-3,top+(y+1)*cell-3,p);}
    }
    // goal
    p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(4);p.setColor(Color.WHITE);c.drawCircle(left+5.5f*cell,top+.5f*cell,cell*.25f,p);
    // player
    p.setStyle(Paint.Style.FILL);p.setColor(Color.WHITE);c.drawCircle(left+(px+.5f)*cell,top+(py+.5f)*cell,cell*.22f,p);
    text.setTextSize(Math.max(12,w*.034f)); text.setColor(Color.WHITE);
    c.drawText("حرکت: "+moves,w/2,top+side+34,text);
    text.setTextSize(Math.max(11,w*.029f)); text.setColor(Color.LTGRAY); c.drawText("سوایپ کن یا با کلیدهای جهت‌دار حرکت بده",w/2,top+side+58,text);
    if(won){p.setColor(Color.argb(225,0,0,0));p.setStyle(Paint.Style.FILL);c.drawRect(0,0,w,h,p);text.setColor(Color.WHITE);text.setTextSize(w*.075f);c.drawText("مانع، ماده‌ی کار تو بود.",w/2,h*.43f,text);text.setTextSize(w*.043f);c.drawText("«آنچه مقاومت می‌کند، تو را شکل می‌دهد.»",w/2,h*.50f,text);text.setTextSize(w*.035f);c.drawText(level==4?"پنج مرحله تمام شد. دوباره بساز.":"برای مرحله بعد لمس کن",w/2,h*.58f,text);}
    p.setStyle(Paint.Style.FILL);
  }
  void move(int dx,int dy){ if(won)return; int nx=px+dx,ny=py+dy; if(nx<0||nx>=N||ny<0||ny>=N)return; if(wall[ny][nx]){ // resistance: first impact removes the wall
      wall[ny][nx]=false; moves++; invalidate(); return;
    } px=nx;py=ny;moves++; if(px==5&&py==0){won=true;} invalidate(); }
  public boolean onTouchEvent(android.view.MotionEvent e){ if(e.getAction()==MotionEvent.ACTION_DOWN){downX=e.getX();downY=e.getY();return true;} if(e.getAction()==MotionEvent.ACTION_UP){float dx=e.getX()-downX,dy=e.getY()-downY; if(won){if(level<4){level++;reset();}else reset();return true;} if(Math.max(Math.abs(dx),Math.abs(dy))<35)return true; if(Math.abs(dx)>Math.abs(dy))move(dx>0?1:-1,0);else move(0,dy>0?1:-1);return true;} return true; }
}
