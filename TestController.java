package com.cs309.quoridorApp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;

@RestController
@RequestMapping("/api")
public class TestController {

    private Session b;

	/*
	@PostMapping("/newGame")
	public String newGame(@RequestParam(value = "xsize", defaultValue = "9") Integer x, @RequestParam(value = "ysize", defaultValue = "9") Integer y) {

		Game game = GameHolder.newGame();

		return "added a new board with id " + game.id;
	}

	@PostMapping("/newPlayer")
	public String newPlayer(@RequestParam(value = "boardid", defaultValue = "-1") Integer bId, @RequestParam(value = "x", defaultValue = "0") Integer xx, @RequestParam(value = "y", defaultValue = "0") Integer yy) {

		Game game = GameHolder.getGame(bId);

		Player p = new Player();
		boolean couldAdd = game != null && game.addPlayer(p, yy, xx);

		return couldAdd ? "could add new player with id " + p.id + " to board" : "couldnt add new player to board" + " with id " + bId;
	}

	@PostMapping("/newWall")
	public String newPlayer(@RequestParam(value = "boardid", defaultValue = "-1") Integer bId, @RequestParam(value = "playerid", defaultValue = "-1") Integer pId, @RequestParam(value = "x1", defaultValue = "0") Integer x, @RequestParam(value = "y1", defaultValue = "0") Integer y, @RequestParam(value = "x2", defaultValue = "0") Integer xx, @RequestParam(value = "y2", defaultValue = "0") Integer yy) {

		Board b = null;

		Player p = b.PLAYERS.get(pId).getPlayer();

		Wall wall = new Wall(p, b.tiles[x][y], b.tiles[xx][yy]);
		boolean couldAdd = b != null && p != null && wall.validWall;

		return couldAdd ? "could add new wall to board" : "couldnt add new wall to board" + " with id " + bId +
				b.tiles[x][y].getDistance(b.tiles[xx][yy]);
	}

	@GetMapping("/getBoard")
	public String getBoard(@RequestParam(value = "boardid", defaultValue = "-1") Integer bId) {

		Board b = null;

		String toReturn = "";

		if(b!=null)
		{
			for(Tile[] column : b.tiles) {
				for (Tile singleRow : column)
					toReturn += "  " + singleRow.toString();
				toReturn +="\n";
			}
		}

		return toReturn;
	}

	//tutorial stuff
	@GetMapping("/getTest")
	public String getTest(@RequestParam(value = "username", defaultValue = "World") String message) {
		return String.format("Hello, %s! You sent a get request with a parameter!", message);
	}
	
	@PostMapping("/postTest1")
	public String postTest1(@RequestParam(value = "username", defaultValue = "World") String message) {
		return String.format("Hello, %s! You sent a post request with a parameter!", message);
	}


	@PostMapping("/postTest2")
	public String postTest2(@RequestBody TestData testData) {
		return String.format("Hello, %s! You sent a post request with a requestbody!", testData.getMessage());
	}

	@GetMapping("/people/{firstName}")
	public @ResponseBody Person getPerson(@PathVariable String firstName) {
		Person p = peopleList.get(firstName);
		return p;
	}


	@DeleteMapping("/deleteTest")
	public void deleteTest() {
	}
	
	@PutMapping("/putTest")
	public void putTest() {
	}

	 */
}
