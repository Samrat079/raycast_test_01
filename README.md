# Raycast Test 01

> 🎮 A simple Java raycasting engine inspired by classic 90s shooters like **Wolfenstein 3D** — built completely from scratch in pure Java with no external build tools.

<div align="center">
  <video width="600" autoplay loop muted playsinline>
    <source src="https://github.com/user-attachments/assets/d6b5cd1c-afd1-4fa7-952a-07965e8c4be7" type="video/mp4">
  </video>
</div>
---

## 🧠 About the Project

This project is a lightweight **raycasting engine written entirely in Java**.
It recreates the rendering technique used in early first-person games to simulate a 3D world using only a 2D grid map.

There are:

* ❌ No game engines
* ❌ No OpenGL / 3D libraries
* ❌ No Maven or Gradle
* ✔ Just raw Java and math

The goal of this project is educational — to understand how early 3D games worked internally.

---

## 🖼️ What is Raycasting?

Raycasting is a rendering technique that creates a 3D perspective from a 2D map.

Instead of rendering full 3D geometry, the engine:

1. Treats the world as a 2D grid (top-down map).
2. Casts rays from the player’s position outward.
3. Detects where each ray hits a wall.
4. Uses the distance to that wall to determine how tall a vertical slice should be drawn on screen.

By repeating this for every vertical column of pixels on the screen, the engine produces a convincing first-person 3D illusion.

This is the same fundamental technique used in **Wolfenstein 3D** and other early FPS games.

---

## ⚙️ How the Engine Works

### 1️⃣ Player State

The player has:

* X and Y position
* Viewing direction
* Field of View (FOV)

### 2️⃣ Ray Casting

For each column of pixels on the screen:

* A ray is cast at a specific angle
* The ray steps through the grid until it hits a wall
* The distance to the wall is calculated

### 3️⃣ Perspective Projection

The wall slice height is computed using:

```
wallHeight ∝ 1 / distance
```

Closer walls appear taller, farther walls shorter.

### 4️⃣ Rendering

Each ray draws one vertical line on screen.
Together, these lines form the 3D scene.

---

## ✨ Features

* ✔ 2D grid-based world
* ✔ Real-time raycasting
* ✔ Player movement
* ✔ Perspective wall rendering
* ✔ Pure Java implementation
* ✔ Automated JAR releases via GitHub Actions

---

## 📦 Download & Run (Prebuilt JAR)

You **do not need to build the project manually**.

This repository includes a GitHub Actions workflow that automatically builds a runnable `.jar` file for each release.

### 🔽 Download

1. Go to the **Releases** section of the repository.
2. Download the latest `.jar` file.

### ▶ Run

Make sure you have Java installed, then run:

```bash
java -jar raycast_test_01.jar
```

If Java is installed correctly, the window should open immediately.

---

## 🛠️ Build Manually (Optional)

If you prefer compiling yourself:

```bash
javac *.java
jar cfe raycast_test_01.jar Main *.class
java -jar raycast_test_01.jar
```

(No Maven or Gradle required.)

---

## 🚀 Future Improvements

Possible next upgrades:

* Textured walls
* Floor & ceiling rendering
* Sprite rendering (enemies, objects)
* Minimap
* Lighting & shading
* Collision improvements

---

## 🎯 Purpose

This project exists to understand the mathematics behind early 3D rendering engines and to explore how much can be achieved using only core Java and simple geometry.

It’s a learning project focused on:

* Graphics fundamentals
* Rendering pipelines
* Perspective math
* Game engine basics