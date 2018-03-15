# SteelTimer
Simple multi-timer, with the ability to add more time in 80 minute increments.

It's all for a game I play.

http://ringofbrodgar.com/wiki/Bar_of_Steel

To make acquire steel, you have to have wrought iron in a lit finery forge for 56 hours, or 2 days and 8 hours, represented by the finished product timer. The finery forge can only hold 12 hours worth of fuel at a time, represented by the current burn timer, and must be refilled before it runs out or else the fire will go out and reset the steel making process.

You can replace coal at the rate of 1 coal every 80 minutes, represented by the coal burn timer.

I wrote this to be able to know how long until I'm done with it, while also being able to keep track of my current fuel level, with the ability to add time by 80 minute increments.

Push start once your finery forge is filled with 9 coals. When you add coal to the forge, push the add coal button for each coal you put in.
Can go past fuel limits if you push at the wrong time, so only push when you need it.

*Needs*
-Preventative coding to stop people from filling past 12 hours.
-State saving. This timer needs to run for 2 days and 8 hours, so if it resets or any timing data is lost in that time it would be not good. There is a onSaveInstanceState method, but the data can be lost if the memory is needed.

-Different values for certain aspects of the game i.e. Total number of coals used / total number needed, 
current fuel level / max fuel level, percentage of steel completion.
