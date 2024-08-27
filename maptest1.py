'''
Features:

Window
Static map background
Scroll in an out
Pan over map
Press a button to return to view
Send camera to different areas of map
Select areas on map and make gui appear
Map animations

'''
'''

import sys
import os

import tkinter as tk
from tkinter import *
from tkinter import messagebox
from tkinter import ttk
from tkinter.font import Font

from PIL import ImageTk, Image

from typing import List, Tuple, NoReturn, Dict

# make a window

class rootWindow(tk.Tk):
    def __init__(self):
        tk.Tk.__init__(self)
        
        self.title("Map Game Test")
        self.attributes("-fullscreen", True)
        self.state("iconic")
        self.configure(background="#FFFFFF")
        self.resizable(False, False)
        self.pack_propagate(0)
        self.protocol("WM_DELETE_WINDOW", on_closing)

        container: Frame = tk.Frame(self)
        container.pack(side = "top", fill = "both", expand = True)

        self.frames: Dict[Frame] = {}
        
        for F in (map_frame):
            frame = F(container, self)
            frame.pack_propogate(0)
            self.frames[F] = frame
            frame.grid(row = 0, column = 0, sticky = "nsew")
        
        frame = map_frame(container, self)
        frame.pack_propagate(0)
        self.frames[map_frame] = frame
        frame.grid(row = 0, column = 0, sticky = "nsew")

    def show_frame(self, controller):
        frame = self.frames[controller]
        frame.tkraise()

class map_frame(tk.Frame):
    def __init__(self, parent: Tk, controller: Tk):
        tk.Frame.__init__(self, parent, bg = "#000000")

        map_image = ImageTk.PhotoImage(Image.open("gfx/United_States_of_America.png"))
        map = tk.Label(self, image = map_image)
        map.place(x = 0, y = 0)

def on_closing() -> NoReturn:
    sys.exit(0)

def main() -> None:
    root = rootWindow()
    root.mainloop()

    print("finished")

if __name__ == "__main__":
    main()'''

import sys
from tkinter import *

import pygame

def on_closing():
    sys.exit(0)

pygame.init()


root = Tk()
root.title("Map Game Test")
root.attributes("-fullscreen", True)
root.state("iconic")
root.configure(background="#FFFFFF")
root.resizable(False, False)
root.pack_propagate(0)
root.protocol("WM_DELETE_WINDOW", on_closing)

map = PhotoImage(file = "gfx/United_States_of_America.png")
label = Label(image = map).pack()

root.mainloop()