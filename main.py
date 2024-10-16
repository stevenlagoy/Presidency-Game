import sys
import os
# import tkinter
import tkinter as tk
from tkinter import *
from tkinter import messagebox
from tkinter import ttk
from tkinter.font import Font

# import other modules
from PIL import ImageTk, Image
import ctypes as ct
import random as rand
import math as math
import json as json
import time as time
from typing import List, Tuple, NoReturn, Dict
# import program classes
from character import Character
from state import State # generates all states
from convention import Primary
from bloc import Bloc, blocs # generates all blocs
from engine import *
from localization.EN_descriptions import EN_descriptions
from localization.EN_system_text import EN_system_text

SAVEDIR: str = os.path.abspath(__file__).replace("main.py", "savegames\\") # constant directory of saves folder
'''
def get_curr_screen_geometry() -> str:
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

def dark_title_bar(window: Tk) -> None:
    \''' Makes a window have a standard Windows dark topbar. Takes Tk window instance. \'''
    window.update()
    set_window_attribute = ct.windll.dwmapi.DwmSetWindowAttribute
    get_parent = ct.windll.user32.GetParent
    hwnd = get_parent(window.winfo_id())
    value = 2
    value = ct.c_int(value)
    set_window_attribute(hwnd, 20, ct.byref(value), 4)
    # only works sometimes????
'''

class rootWindow(tk.Tk):
    def __init__(self):
        tk.Tk.__init__(self)

        # set some attributes of the window
        self.title(EN_system_text.get("title"))
        self.attributes("-fullscreen", True)
        self.state("iconic")
        self.configure(background="#808080")
        #dark_title_bar(root)
        self.iconbitmap("gfx\\icon.ico")
        self.resizable(False, False)
        self.pack_propagate(0)
        self.protocol("WM_DELETE_WINDOW", on_closing) # stops the whole program when the tk window is closed

        # create a container to hold the frames
        container: Frame = tk.Frame(self)
        container.pack(side = "top", fill = "both", expand = True)
    
        # dictionary for the frames
        self.frames: Dict[Frame] = {}

        # create the frames and add to dictionary
        for F in (menu_frame, openSave_frame, newGame_frame, characterRoll_frame):
            frame = F(container, self)
            frame.pack_propagate(0)
            self.frames[F] = frame
            frame.grid(row = 0, column = 0, sticky = "nsew")
        
        self.show_frame(menu_frame)
    
    def show_frame(self, controller):
        frame = self.frames[controller]
        frame.tkraise()

class menu_frame(tk.Frame):
    '''Creates the menu frame.'''
    def __init__(self, parent: Tk, controller: Tk):
        from fonts import fonts
        tk.Frame.__init__(self, parent, bg = "#808080", highlightbackground = "black", highlightthickness = 4)

        background_image = ImageTk.PhotoImage(Image.open("gfx/loadscreens/mount_rushmore.png").resize((2560,1440)))
        background = tk.Label(self, image = background_image)
        background.image = background_image
        #background.grid(row = 0, column = 0) need to be able to put the image behind the other gridded elements
        #background.lower()
        
        titlecard = ImageTk.PhotoImage(Image.open("gfx/title_card.png").resize((606,300)))
        title_label = tk.Label(self, image = titlecard)
        title_label.image = titlecard
        title_label.grid(row = 0, column = 0, pady = 10)

        new_save_button = tk.Button(self, font = fonts.button, text = EN_system_text.get("new_game_text"), width = 50,
            command = lambda: controller.show_frame(newGame_frame))
        new_save_button.grid(row = 1, column = 0, padx = 10, pady = 10)

        continue_button = tk.Button(self, font = fonts.button, text = EN_system_text.get("continue_game_text"), width = 50,
            command = lambda: controller.show_frame(openSave_frame))
        continue_button.grid(row = 2, column = 0, padx = 10, pady = 10)

        tutorial_button = tk.Button(self, font = fonts.button, text = EN_system_text.get("tutorial_text"), width = 50)
        tutorial_button.grid(row = 3, column = 0, padx = 10, pady = 10)

        about_button = tk.Button(self, font = fonts.button, text = EN_system_text.get("about_text"), width = 50)
        about_button.grid(row = 4, column = 0, padx = 10, pady = 10)

        close_game_button = tk.Button(self, font = fonts.button, text = EN_system_text.get("close_game_text"), width = 50,
            command = lambda: check_exit_game(parent.master))
        close_game_button.grid(row = 5, column = 0, padx = 10, pady = 10)
    
class openSave_frame(tk.Frame):
    '''Creates the open save menu.'''
    def __init__(self, parent, controller):
        tk.Frame.__init__(self)

        '''save_list_info_label = tk.Label(self, text = "Name\t\t\tDate\t\t\tProgress", justify = "left", anchor = "w", width = 80, height = 0)
        save_list_info_label.grid(row = 0, column = 0)

        saves_list = tk.Listbox(self, width = 100)
        count = 1
        for file in os.listdir(SAVEDIR):
            saves_list.insert(count, str(file).split(".")[0] + "             " + get_save_info(SAVEDIR + str(file))[1])
        # i want the date and "progress" to be printed in the list as well... this poses an interesting problem, since they need to line up, and the saves may have a very long name
        
        # this has to be a lambda i think... there might be a better way
        # get some info about the file and set the label's text accordingly - i hate to call the function twice but i'm not sure how to use the returned values otherwise
        saves_list.bind('<<ListboxSelect>>', lambda x: save_info_label.config(text =
            "Save Name: " + get_save_info(SAVEDIR + saves_list.get(saves_list.curselection()) + ".txt")[0] +
            "\nSave Date: " + get_save_info(SAVEDIR + saves_list.get(saves_list.curselection()) + ".txt")[1]
            ))
        saves_list.grid(row = 1, column = 0)

        save_info_label = tk.Label(self, text = "Save Name:\nSave Date:", width = 25, height = 2, anchor = "w", justify = "left")
        save_info_label.grid(row = 2, column = 0)

        open_save_button = tk.Button(self, text = EN_system_text.get("open_game_text"),
            command = lambda : open_save(SAVEDIR + saves_list.get(saves_list.curselection()) + ".txt"))
        open_save_button.grid(row = 3, column = 0)

        saves_list_back_button = tk.Button(self, text = EN_system_text.get("back_text"),
            command = lambda: controller.show_frame(menu_frame))
        saves_list_back_button.grid(row = 4, column = 0)'''

class newGame_frame(tk.Frame):
    '''Creates the new game menu.'''
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        self.pack_propagate(False)

        next_button = tk.Button(self, text = "Continue", command = lambda : controller.show_frame(characterRoll_frame))
        next_button.grid(row = 0, column = 0)

        '''
        new_game_back_button = tk.Button(self, text = EN_system_text.get("back_text"),
            command = lambda: controller.show_frame(menu_frame))
        new_game_back_button.grid(row = 0, column = 0)

        start_game_button = tk.Button(self, text = "Announce Candidacy",
            command = lambda: controller.show_frame())
        start_game_button.grid(row = 1, column = 0)
        '''

class characterRoll_frame(tk.Frame):
    '''Screen to create a character.'''
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent, width = 100, height = 100, bg = "#808080")
        self.pack_propagate(False)

        givenName_entry = tk.Entry(self)
        givenName_entry.grid(row = 0, column = 0)

        middleName_entry = tk.Entry(self)
        middleName_entry.grid(row = 0, column = 1)

        familyName_entry = tk.Entry(self)
        familyName_entry.grid(row = 0, column = 2)

        next_button = tk.Button(text = "Continue")
        next_button.grid(row = 1, column = 0)

class viewSwitcher_frame(tk.Frame):
    '''Creates the view switcher menu.'''
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent, width = self.winfo_width(), height = 200, bg = "green")
        self.pack_propagate(False)

        '''
        character_view_button = tk.Button(self, text = "Character View",
            command = lambda: controller.show_frame())
        character_view_button.pack(side = "left")

        party_view_button = tk.Button(self, text = "Party View",
            command = lambda: controller.show_frame())
        party_view_button.pack(side = "left")

        race_view_button = tk.Button(self, text = "Race View", command = ...)
        race_view_button.pack(side = "left")

        map_view_button = tk.Button(self, text = "Map View",
            command = lambda: controller.show_frame())
        map_view_button.pack(side = "left")

        blocs_view_button = tk.Button(self, text = "Blocs View",
            command = lambda: controller.show_frame())
        blocs_view_button.pack(side = "left")

        settings_button = tk.Button(self, text = "Settings")
        settings_button.pack(side = "left")
        '''

class characterView_frame(tk.Frame):
    '''Creates the character view frame.'''
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        #self.bind("<<ShowFrame>>", lambda x : viewSwitcher_frame.pack(side = "bottom", fill = "x"))

        '''image1 = Image.open("gfx\\empty_portrait.png")
        portrait_label = tk.Label(self, image = ImageTk.PhotoImage(image1))
        portrait_label.image = ImageTk.PhotoImage(image1)
        portrait_label.place(x=100, y=100)
        portrait_label.pack()'''

class partyView_frame(tk.Frame):
    '''Creates the party view frame.'''
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        #self.bind("<<ShowFrame>>", lambda x : viewSwitcher_frame.pack(side = "bottom", fill = "x"))

class mapView_frame(tk.Frame):
    '''Creates the map view frame.'''
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        #self.bind("<<ShowFrame>>", lambda x : viewSwitcher_frame.pack(side = "bottom", fill = "x"))

class blocsView_frame(tk.Frame):
    '''Creates the blocs view frame.'''
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        #self.bind("<<ShowFrame>>", lambda x : viewSwitcher_frame.pack(side = "bottom", fill = "x"))

def check_exit_game(root) -> bool:
    '''Check that a user wishes to exit the game. Returns true if exit confirmed.'''
    MsgBox = messagebox.askyesno(EN_system_text.get("close_game_text"), EN_system_text.get("close_game_alert_text"), icon = 'warning')
    if MsgBox:
        root.destroy()
        return True
    return False

def on_closing() -> NoReturn:
    '''Called when the root window is closed. Exits the program.'''
    sys.exit(0)

def openFrame(root: Tk, frame_to_open: Frame, frame_name: str = "") -> bool:
    '''Opens a frame in the root window. Returns true if opened successfully.'''
    # open the menu frame
    # takes the active Tk window
    if frame_to_open is None:
        print(f"open frame is None with object {frame_to_open} name {frame_name}")
        return False
    clearWindow(root)
    if frame_name == "menu_frame":
        frame_to_open.place(x = 100, y = 400)
    else:
        frame_to_open.grid(row = 0, column = 0)
    frame_to_open.event_generate("<<ShowFrame>>")
    return True

def clearWindow(root):
    # clear a Tk window
    for frame in root.winfo_children():
        if frame.widgetName == 'frame':
            frame.pack_forget()
            frame.grid_forget()

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

def update_probability(actual, nominal, outcome, weighted, target):
    # takes the actual probability of a 'winning' outcome (determined by the roll function)
    # takes the nominal probability of a 'winning' outcome (what is shown to the user)
    # takes the outcome of the previous roll, 0 for a 'losing' outcome and 1 for a 'winning' outcome
    # takes the current gloabl weighted probability, wich is influenced by all past outcomes
    # takes the target probability for the weighted - what value the weighted probability is intended to approach
    
    deviation = outcome - weighted # the difference between the outcome and the expected value
    ...

def roll_probability(nominal, modifier):
    additive = nominal * modifier
    scale = 1 - nominal
    scaled_additive = additive * scale
    weighted = nominal + scaled_additive
'''
# create all frame pointers so they can be accessed statically / globally
menu_frame: Frame|None = None
openSave_frame: Frame|None = None
newGame_frame: Frame|None = None
viewSwitcher_frame: Frame|None = None
characterView_frame: Frame|None = None
partyView_frame: Frame|None = None
mapView_frame: Frame|None = None
blocsView_frame: Frame|None = None
'''
def main() -> None:
    ''' main.py '''
    print("savedir " + SAVEDIR)

    # call the reset function
    reset()

    # create the root window
    root = rootWindow()
    root.mainloop()

    reset()
    
    print("Finished")

if __name__ == "__main__":
    main()