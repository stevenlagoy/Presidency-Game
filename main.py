import sys
import os
import tkinter as tk
from tkinter import *
from tkinter import messagebox
from tkinter import ttk
from PIL import ImageTk, Image
from tkinter.font import Font
import ctypes as ct
import random as rand
import math as math
import json as json
import time as time
from character import Character
from state import State # generates all states
from convention import Primary
from bloc import Bloc, blocs # generates all blocs

SAVEDIR = "C:\\Users\\LaGoySM\\Downloads\\Documents\\Presidency Game\\savegames\\" # constant directory of saves folder

def get_curr_screen_geometry():
  # find the size (in pixels) of current active screen even when there are multiple monitors
  # returns geometry: Tk geometry string [width]x[height]+[left]+[top]

  root = tk.Tk()
  root.update_idletasks()
  root.attributes('-fullscreen', True)
  root.state('iconic')
  geometry = root.winfo_geometry()
  root.destroy()
  print(geometry)
  return geometry

def dark_title_bar(window):
  # make a window have a dark windows topbar
  # takes an instance of Tk window
  window.update()
  set_window_attribute = ct.windll.dwmapi.DwmSetWindowAttribute
  get_parent = ct.windll.user32.GetParent
  hwnd = get_parent(window.winfo_id())
  value = 2
  value = ct.c_int(value)
  set_window_attribute(hwnd, 20, ct.byref(value), 4)
  # only works sometimes????

def createWindow():
  root = tk.Tk()
  root.title("Race for the Presidency")
  root.attributes("-fullscreen", True)
  root.state("iconic")
  root.configure(background="#808080")
  #dark_title_bar(root)
  root.iconbitmap("gfx\\icon.ico")
  root.resizable(False, False)
  root.pack_propagate(0)

  # fonts
  buttonfont = Font(family = "Yu Gothic Semibold", size = 16)

  # menu frame ----------------------------------------------------------------------------------------------------------------------------------------
  menu_frame = tk.Frame(root, bg = "#808080", highlightbackground = "black", highlightthickness = 4)

  bgimg = ImageTk.PhotoImage(Image.open("gfx\\loadscreens\\mount_rushmore.png"))
  background = tk.Label(root, image = bgimg)
  background.place(x = 0, y = 0)
  background.lower()

  titlecard = ImageTk.PhotoImage(Image.open("gfx/title_card.png").resize((606,300)))
  title_label = tk.Label(menu_frame, image = titlecard)
  title_label.image = titlecard
  title_label.grid(row = 0, column = 0, pady = 10)

  new_save_button = tk.Button(menu_frame, font = buttonfont, text = "New Game", width = 50, command = lambda: openFrame(root, new_game_frame, "new_game_frame"))
  new_save_button.grid(row = 1, column = 0, padx = 10, pady = 10)

  continue_button = tk.Button(menu_frame, font = buttonfont, text = "Continue", width = 50, command = lambda: openFrame(root, open_save_frame, "open_save_frame"))
  continue_button.grid(row = 2, column = 0, padx = 10, pady = 10)

  tutorial_button = tk.Button(menu_frame, font = buttonfont, text = "Tutorial", width = 50)
  tutorial_button.grid(row = 3, column = 0, padx = 10, pady = 10)

  about_button = tk.Button(menu_frame, font = buttonfont, text = "About", width = 50)
  about_button.grid(row = 4, column = 0, padx = 10, pady = 10)

  close_game_button = tk.Button(menu_frame, font = buttonfont, text = "Exit Game", width = 50, command = lambda : check_exit_game(root))
  close_game_button.grid(row = 5, column = 0, padx = 10, pady = 10)
  
  # open save frame ----------------------------------------------------------------------------------------------------------------------------------------
  open_save_frame = tk.Frame(root)

  save_list_info_label = tk.Label(open_save_frame, text = "Name\t\t\tDate\t\t\tProgress", justify = "left", anchor = "w", width = 80, height = 0)
  save_list_info_label.grid(row = 0, column = 0)

  saves_list = tk.Listbox(open_save_frame, width = 100)
  count = 1
  for file in os.listdir(SAVEDIR):
    saves_list.insert(count, str(file).split(".")[0] + "       " + get_save_info(SAVEDIR + str(file))[1])
  # i want the date and "progress" to be printed in the list as well... this poses an interesting problem, since they need to line up, and the saves may have a very long name
  
  # this has to be a lambda i think... there might be a better way
  # get some info about the file and set the label's text accordingly - i hate to call the function twice but i'm not sure how to use the returned values otherwise
  saves_list.bind('<<ListboxSelect>>', lambda x: save_info_label.config(text =
    "Save Name: " + get_save_info(SAVEDIR + saves_list.get(saves_list.curselection()) + ".txt")[0] +
    "\nSave Date: " + get_save_info(SAVEDIR + saves_list.get(saves_list.curselection()) + ".txt")[1]
    ))
  saves_list.grid(row = 1, column = 0)

  save_info_label = tk.Label(open_save_frame, text = "Save Name:\nSave Date:", width = 25, height = 2, anchor = "w", justify = "left")
  save_info_label.grid(row = 2, column = 0)

  open_save_button = tk.Button(open_save_frame, text = "Open Save", command = lambda: open_save(SAVEDIR + saves_list.get(saves_list.curselection()) + ".txt"))
  open_save_button.grid(row = 3, column = 0)

  saves_list_back_button = tk.Button(open_save_frame, text = "Back", command = lambda: openFrame(root, menu_frame, "menu_frame"))
  saves_list_back_button.grid(row = 4, column = 0)

  # new game frame ----------------------------------------------------------------------------------------------------------------------------------------
  new_game_frame = tk.Frame(root)

  new_game_back_button = tk.Button(new_game_frame, text = "Back", command = lambda: openFrame(root, menu_frame, "menu_frame"))
  new_game_back_button.pack()

  start_game_button = tk.Button(new_game_frame, text = "Announce Candidacy", command = lambda: openFrame(root, character_view_frame))
  start_game_button.pack()

  # view switcher frame ----------------------------------------------------------------------------------------------------------------------------------------
  view_switcher_frame = tk.Frame(root, width = root.winfo_width(), height = 200, bg = "green")
  view_switcher_frame.pack_propagate(False)

  character_view_button = tk.Button(view_switcher_frame, text = "Character View", command = lambda: openFrame(root, character_view_frame))
  character_view_button.pack(side = "left")

  party_view_button = tk.Button(view_switcher_frame, text = "Party View", command = lambda: openFrame(root, party_view_frame))
  party_view_button.pack(side = "left")

  race_view_button = tk.Button(view_switcher_frame, text = "Race View", command = ...)
  race_view_button.pack(side = "left")

  map_view_button = tk.Button(view_switcher_frame, text = "Map View", command = lambda: openFrame(root, map_view_frame))
  map_view_button.pack(side = "left")

  blocs_view_button = tk.Button(view_switcher_frame, text = "Blocs View", command = lambda: openFrame(root, blocs_view_frame))
  blocs_view_button.pack(side = "left")

  settings_button = tk.Button(view_switcher_frame, text = "Settings")
  settings_button.pack(side = "left")

  # character view frame ----------------------------------------------------------------------------------------------------------------------------------------
  character_view_frame = tk.Frame(root)
  character_view_frame.bind("<<ShowFrame>>", lambda x : view_switcher_frame.pack(side = "bottom", fill = "x"))

  image1 = Image.open("gfx\\empty_portrait.png")
  portrait_label = tk.Label(character_view_frame, image = ImageTk.PhotoImage(image1))
  portrait_label.image = ImageTk.PhotoImage(image1)
  portrait_label.place(x=100, y=100)
  portrait_label.pack()

  # party view frame
  party_view_frame = tk.Frame(root)
  party_view_frame.bind("<<ShowFrame>>", lambda x : view_switcher_frame.pack(side = "bottom", fill = "x"))

  # map view frame
  map_view_frame = tk.Frame(root)
  map_view_frame.bind("<<ShowFrame>>", lambda x : view_switcher_frame.pack(side = "bottom", fill = "x"))

  # blocs view frame
  blocs_view_frame = tk.Frame(root)
  blocs_view_frame.bind("<<ShowFrame>>", lambda x : view_switcher_frame.pack(side = "bottom", fill = "x"))

  # set up window ----------------------------------------------------------------------------------------------------------------------------------------
  openFrame(root, menu_frame, "menu_frame")

  root.protocol("WM_DELETE_WINDOW", on_closing) # stops the whole program when the tk window is closed
  root.mainloop()

  return root

def check_exit_game(root):
  MsgBox = messagebox.askyesno("Exit Game", "Exit to Desktop?\n(Unsaved data will be lost)", icon = 'warning')
  if MsgBox:
    root.destroy()

def on_closing():
  sys.exit(0)

def openFrame(root, frame_to_open, frame_name = ""):
  # open the menu frame
  # takes the active Tk window
  clearWindow(root)
  if frame_name == "menu_frame":
    frame_to_open.place(x = 100, y = 400)
  else:
    frame_to_open.grid(row = 0, column = 0)
  frame_to_open.event_generate("<<ShowFrame>>")

def clearWindow(root):
  # clear a Tk window
  for frame in root.winfo_children():
    if frame.widgetName == 'frame':
      frame.pack_forget()
      frame.grid_forget()

def rollDice(numDice, sides = 6):
  diceTotal = 0
  for i in range(numDice):
    diceTotal += rand.randint(1,sides)
  return diceTotal

def get_save_info(path):
  # returns a small amount of information about a save game
  # [name, date]

  file = open(path, "r") # open save file in read mode
  return [file.readline()[:-1], file.readline()[:-1]]

def save(savename):
  # append the current game to "saves.txt"
  # takes the name of the save as a string
  '''need to save:
    characters
    states?
    primaries?
    event history
  
  saves should be titled the same as the player character's name, plus the date-time it was saved at
  the save screen should have a checkbox to remove old saves, checking it will remove saves other than the three backups
  the name and date of the save is also included within the file itself, incase the filename gets changed somehow
  '''

  file = open(SAVEDIR + savename, "a") # create save file in append mode
  file.write("SAVE" + str(savename) + "\n") # write the name of the save
  file.write("TIME" + str(int(time.time())) + "\n") # write the time when saved
  file.write("PLAY" + str([char.name for char in Character.instances if char.is_player is True][0]) + "\n") # write the name of the character
  for character in Character.instances: # write the rest of the characters
    file.write(character.__repr__() + "\n")
  file.write("\n") # end line
  file.close() # close the file
  # this should eventually be encrypted and scrambled
  # could make a unique file extension to keep the save files in - requires OS instuctions for opening file

def open_save(path):
  # opens a single save
  # takes the path to a single file
  save_data = ""
  try:
    file = open(path, "r") # open file in read mode
    for line in file.readlines():
      save_data += line if line != "\n" else "" # sort out blank lines
    file.close() # close the file
  except FileNotFoundError:
    pass

  print(save_data)

  # this is where the complicated part goes where it creates all the characters and states and everything based on what's in the file

def reset():
  Character.instances = []

def main():

  reset()

  window = createWindow()
  
  reset()

  print("Finished")

if __name__ == "__main__":
  main()