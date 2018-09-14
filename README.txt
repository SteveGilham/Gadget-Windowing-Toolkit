
                      This is GWT 2.0!

      The Gadget Windowing Toolkit by DTAI Incorporated.

It is distributed for FREE under the Gnu Public License (GPL).
(Please see the accompanying license file.)

This basically means, you can use it, and modify it, but that
we request your assistance in making the product better.  Please
let us know by email if you find any severe bugs, or if you have
found and fixed a bug (and how you fixed it).  Also, if you have
any enhancements, please send us the source code for that.

THE BEST WAY to send us significant changes is to send back to
us the entire distribution.  This includes the unchanged "CVS"
directories.  (It makes it easier for us to check your changes
against our baseline.)

No guarantees we will include anything in our baseline, but if
it makes sense, and we can, we will.  And we appreciate all
attempts to make GWT better.

DTAI uses GWT on several projects DAILY, and we will try to
update the GWT distribution as often as we can.

Thanks!

Rich Kadel
GWT Product Manager

P.S., Some quick technical details...

[Check out our web site for the brief on GWT, and to get the latest
GWT news and updated software.]

GWT replaces just about all of the Java AWT.  (We still use Java Graphics,
Image, Color, Font, Frame, Dialog, Panel, and some of the support
classes like Dimension, Point, Rectangle, and Insets; but I think that's
about it.)

The GWT "Components" are called "Gadgets" in GWT.  Gadgets are actually
all drawn onto one big canvas.  It's fast and flexible because of the
intelligent clipping and gadget management that GWT performs.  It allows
transparent gadgets, tooltips, popup lists and menus, and a bunch of
other features.

Does it sound like the new JFC--Java Foundation Classes (AKA Swing Project)?
It should.  JFC will do the same thing, in just about the same way.
So why use GWT?

JFC is not here yet.  IFC exists, but you may find GWT more to your
liking.  It is pretty darned flexible, and it comes with a standard
Microsoft Windows look-and-feel.  AND MOST IMPORTANT, it runs in JDK
1.0.2 environments!  Significant, because JFC won't, and as of this
writing, widespread 1.1 browsers won't be available until at least
the Fall, several months away.  Also GWT mimics the AWT model, including
the API, layout managers, and the event model.  In fact, the GWT API
is almost identical to the JDK 1.1 AWT API, while running under 1.2.

Documentation is scarce, and the comments don't always match the code,
but the source code is provided!  And you can always use Sun's JDK 1.1
documentation!  It's almost an exact match.  If GWT provides support
(and it does for just about every JDK 1.1 AWT class, and more) then
the only difference should be the class names.  (Usually add Gadget
to the beginning--e.g., GadgetGraphics--if it's a utility class,
or to the end--e.g., ButtonGadget--if it's a Component replacement.)