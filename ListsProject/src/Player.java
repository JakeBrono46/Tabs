
public class Player implements Comparable<Player>{

	private int num;
	private String name;
	private char position;
	private double points, rebounds, assists;
	
	public Player(String Name, int number, char Position, double Pts, double Rbs, double Ast) {
		name = Name;
		num = number;
		position = Position;
		points = Pts;
		rebounds = Rbs;
		assists = Ast;
	}
	
	public String toString() {
		return "Player: " + name + "\n Number: " + num + "\n Position:     " + position + "\n PTS/Game:     " + points + "\n RBS/Game:     " + rebounds + "\n AST/Game:     " + assists;
	}
	
	public String toFile() {
		return name + " " + num + " " + position + " " + points + " " + rebounds + " " + assists + " ";
	}
	
	public String getName() {
		return name;
	}
	
	public int getNum() {
		return num;
	}
	
	public String getPosition() {
		if(position == 'C') {
			return "Center";
		}
		
		if(position == 'F') {
			return "Forward";
		}
		
		if(position == 'G') {
			return "Guard";
		}
		
		if(position == 'P') {
			return "Point Guard";
		}
		
		return "Invalid Position";
	}
	
	public double getPoints() {
		return points;
	}
	
	public double getRebounds() {
		return rebounds;
	}
	
	public double getAssists() {
		return assists;
	}

	public int compareTo(Player p) {
		if(num > p.num) {
			return 1;
		}else if(num < p.num) {
			return -1;
		}else {
			return 0;
		}
	}
}
