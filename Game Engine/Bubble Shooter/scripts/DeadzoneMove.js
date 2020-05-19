function move() {
	if((deadzone.x > (deadzone.parent.width-deadzone.radius)) || (deadzone.x < deadzone.radius))
		deadzone.xspeed *= -1;
	
	if((deadzone.y > (deadzone.parent.height-deadzone.radius)) || (deadzone.y < deadzone.radius))
		deadzone.yspeed *= -1;
	
	deadzone.x += (deadzone.xspeed*deadzone.frameDuration);
	deadzone.y += (deadzone.yspeed*deadzone.frameDuration);
}