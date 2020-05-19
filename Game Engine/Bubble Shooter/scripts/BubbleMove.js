function move(bubble) {
	if((bubble.x > ((bubble.parent).width-bubble.radius)) || (bubble.x < bubble.radius))
			bubble.xspeed *= -1;
		
	bubble.x += (bubble.xspeed*bubble.frameDuration);
	bubble.y += (bubble.yspeed*bubble.frameDuration);
}