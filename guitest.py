import tkinter as tk
import sys

def create_window():
    root = tk.Tk()
    root.title("GUI Test")
    root.configure(background="#101010")
    root.state("zoomed")

    green_frame = tk.Frame(root, bd = 1, bg = "green", width = 100, height = 100)
    green_frame.pack_propagate(False)

    blue_frame = tk.Frame(root, bd = 1, bg = "blue", width = root.winfo_width(), height = 200)
    blue_frame.pack_propagate(False)

    red_frame = tk.Frame(root, bd = 1, bg = "red", width = 100, height = 100)
    red_frame.bind("<<ShowFrame>>", lambda x : blue_frame.pack(side = "bottom", fill = "x"))
    red_frame.pack_propagate(False)
    
    green_frame_button = tk.Button(red_frame, text = "Open Green", command = lambda : openFrame(root, green_frame))
    green_frame_button.pack()

    red_frame_button = tk.Button(green_frame, text = "Open Red", command = lambda : openFrame(root, red_frame))
    red_frame_button.pack()

    button1 = tk.Button(blue_frame, text = "Button 1", width = 50, height = 200, anchor = "w", command = ...)
    button1.pack(side = "left", fill = "x")

    button2 = tk.Button(blue_frame, text = "Button 2", width = 50, height = 200, anchor = "w", command = ...)
    button2.pack(side = "left")

    button3 = tk.Button(blue_frame, text = "Button 3", width = 50, height = 200, anchor = "w", command = ...)
    button3.pack(side = "left")

    button4 = tk.Button(blue_frame, text = "Button 4", width = 50, height = 200, anchor = "w", command = ...)
    button4.pack(side = "left")

    root.protocol("WM_DELETE_WINDOW", on_closing) # stops the whole program when the tk window is closed
    green_frame.pack(side = "top", fill = "none")
    green_frame.event_generate("<<ShowFrame>>")
    root.mainloop()

def on_closing():
  sys.exit(0)

def openFrame(root, frame_to_open):
  # open the menu frame
  # takes the active Tk window
  clearWindow(root)
  frame_to_open.event_generate("<<ShowFrame>>")
  frame_to_open.pack()

def clearWindow(root):
  # clear a Tk window
  for frame in root.winfo_children():
    if frame.widgetName == 'frame':
      frame.pack_forget()

def main():
    create_window()

if __name__ == "__main__":
    main()