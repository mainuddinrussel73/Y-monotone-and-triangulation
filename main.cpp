//
//  main.cpp
//  project
//
//  Created by MacBookAir on 11/12/18.
//  Copyright © 2018 MacBookAir. All rights reserved.
//
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<iostream>
#include <unistd.h>

#include <OpenGL/gl.h>

#include <OpenGl/glu.h>

#include <GLUT/glut.h>
#include <fstream>
std::ifstream infile("/Users/macbookair/IdeaProjects/monotone/src/offline/out");

using namespace std;

#define pi (2*acos(0.0))

struct point2d
{
    double x, y;
};

struct point2d cp[200];
struct point2d cpextra[200];
struct point2d cpextra1[200];
int cpidx;
int cpidx2;
int cpidx3;

void drawAxes()
{
    
    glColor3f(1.0, 1.0, 1.0);
    glBegin(GL_LINES);{
        glVertex3f(500,0,0);
        glVertex3f(-500,0,0);
        
        glVertex3f(0,-500,0);
        glVertex3f(0, 500,0);
        
        glVertex3f(0,0, 500);
        glVertex3f(0,0,-500);
    }glEnd();
    
}


void drawSquare()
{
    glBegin(GL_QUADS);
    {
        glVertex3d( 1,  1, 0);
        glVertex3d( 1, -1, 0);
        glVertex3d(-1, -1, 0);
        glVertex3d(-1,  1, 0);
    }
    glEnd();
}
void drawSquare1()
{
    glBegin(GL_QUADS);
    {
        glVertex3d( 2,  2, 0);
        glVertex3d( 2, -2, 0);
        glVertex3d(-2, -2, 0);
        glVertex3d(-2,  2, 0);
    }
    glEnd();
}


void display(){
    
    //clear the display
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glClearColor(0,0,0,0);    //color black
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    gluLookAt(0,0,200,    0,0,0,    0,1,0);
    glMatrixMode(GL_MODELVIEW);
    
    
    int i;
    
    for (i = 0; i < cpidx; i++)
    {
        glColor3f(1, 1, 0);
        glPushMatrix();
        {
            glTranslatef(cp[i].x, cp[i].y, 0);
            drawSquare();
        }
        glPopMatrix();
    }
   
    
    
    
    
    for (i=0; i<cpidx-1; i++) {
        glColor3f(1, 1, 1);
        glBegin(GL_LINE_STRIP);{
            glVertex3d(cp[i].x,cp[i].y,0);
            glVertex3d(cp[i+1].x,cp[i+1].y,0);
        }glEnd();
    }
    glColor3f(1, 1, 1);
    glBegin(GL_LINE_STRIP);{
        glVertex3d(cp[0].x,cp[0].y,0);
        glVertex3d(cp[cpidx-1].x,cp[cpidx-1].y,0);
    }glEnd();
    
    
   for (i=0; i<cpidx2-1; i+=2) {
        glColor3f(1, 0, 1);
        glBegin(GL_LINE_STRIP);{
            glVertex3d(cpextra[i].x,cpextra[i].y,0);
            glVertex3d(cpextra[i+1].x,cpextra[i+1].y,0);
    
        }glEnd();
   }

    
   
    
    
    drawAxes();
    glutSwapBuffers();
}


void animate(){
    glutPostRedisplay();
}

void init(){
    
    
    
    cpidx = 0;
    int points;
    infile>>points;
    for (int i = 0; i<points; i++) {
        double x,y;
        infile>>x>>y;
        cp[cpidx].x = (double)x*10;
        cp[cpidx].y = (double) y*10;
        cpidx++;
    }
    
    cpidx2 = 0;
    int points2;
    infile>>points2;
    for (int i = 0; i<points2; i++) {
        double x,y;
        infile>>x>>y;
        cpextra[cpidx2].x = (double)x*10;
        cpextra[cpidx2].y = (double) y*10;
        cpidx2++;
    }
    
    
    
    glClearColor(0,0,0,0);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluPerspective(80,    1,    1,    1000.0);
    
}



int main(int argc, char **argv){
    glutInit(&argc,argv);
    glutInitWindowSize(1000, 1000);
    glutInitWindowPosition(0, 0);
    glutInitDisplayMode(GLUT_DEPTH | GLUT_DOUBLE | GLUT_RGB);
    
    glutCreateWindow("My OpenGL Program");
    
    init();
    
    glEnable(GL_DEPTH_TEST);
    
    glutDisplayFunc(display);
    glutIdleFunc(animate);
    
    
    
    glutMainLoop();
    
    return 0;
}

