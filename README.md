# Bach
Compose components using a generic designer.

![Logo](logo/logo.svg)

## Overview
Sometimes you need a relatively simple, low-level, generic designer library that's open to extension. Bach is designed to enable you to build interconnected diagrams of *bricks* (some sort of image representing an attached object) with a simple mechanism for defining rules about which sorts of brick can connect to which other sorts etc. This code was extracted from the [Denobo](https://github.com/lambdacasserole/Denobo) network designer utility and made as generic as possible.

![Preview](preview.gif)

## Prerequisites
Bach is written in Java, and makes use of Swing. Batteries are definitely not included whatsoever, it's entirely up to you to define your own bricks (by subclassing the abstract `Brick` class), handle image loading, and define your object model. Context menus are supported, but once again you'll have to define your own.

## Use Case
The library is designed to be very general-purpose. If you need a starting point for simply modelling the relationships between objects, with some constraints, Bach might be for you.
