function onCollision(surface, gamecharacter, platform){
	if(surface == 1) {
		gamecharacter.yspeed = 0;
		gamecharacter.accelerate = 0;
		gamecharacter.y = platform.y - gamecharacter.radius;
	}else {
		gamecharacter.yspeed *= -1;
		gamecharacter.y = platform.y + platform.height + gamecharacter.radius;
	}
}