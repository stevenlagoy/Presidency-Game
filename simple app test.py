import tkinter as tk
from PIL import ImageTk, Image

class SampleApp(tk.Tk):
    def __init__(self):
        tk.Tk.__init__(self)
        self.title("Multi-Frame Application")
        
        # Create a container to hold all frames
        container = tk.Frame(self)
        container.pack(side="top", fill="both", expand=True)
        
        # Dictionary to hold frames
        self.frames = {}
        
        # Create frames and add them to the dictionary
        for F in (StartPage, Frame1, Frame2):
            frame = F(container, self)
            self.frames[F] = frame
            frame.grid(row=0, column=0, sticky="nsew")
        
        self.show_frame(StartPage)
    
    def show_frame(self, cont):
        frame = self.frames[cont]
        frame.tkraise()

class StartPage(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        label = tk.Label(self, text="Start Page", font=("Arial", 18))
        label.pack(pady=10, padx=10)
        
        titlecard = ImageTk.PhotoImage(Image.open("gfx/loadscreens/mount_rushmore.png").resize((606,300)))
        title_label = tk.Label(self, image = titlecard)
        title_label.image = titlecard
        title_label.pack(padx = 10, pady = 10)

        button1 = tk.Button(self, text="Go to Frame 1",
                            command=lambda: controller.show_frame(Frame1))
        button1.pack()
        
        button2 = tk.Button(self, text="Go to Frame 2",
                            command=lambda: controller.show_frame(Frame2))
        button2.pack()

class Frame1(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        label = tk.Label(self, text="Frame 1", font=("Arial", 18))
        label.pack(pady=10, padx=10)
        
        button = tk.Button(self, text="Back to Home",
                           command=lambda: controller.show_frame(StartPage))
        button.pack()

class Frame2(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        label = tk.Label(self, text="Frame 2", font=("Arial", 18))
        label.pack(pady=10, padx=10)
        
        button = tk.Button(self, text="Back to Home",
                           command=lambda: controller.show_frame(StartPage))
        button.pack()

if __name__ == "__main__":
    app = SampleApp()
    app.mainloop()
