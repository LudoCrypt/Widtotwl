package net.ludocrypt.widtotwl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Level {

	private String readLevel;

	private String[][][] geo;
	private String[][][] tiles;
	private String[] props;

	private String originalGeo;
	private String originalTiles;
	private String originalProps;

	public static Level parse(File file) {
		Level lvl = new Level();

		try {
			lvl.readLevel = new String(Files.readAllBytes(Paths.get(file.getPath())));
			lvl.originalGeo = lvl.readLevel.substring(lvl.readLevel.indexOf("[[[["), lvl.readLevel.indexOf("]]]]]") + 5);
			lvl.geo = Level.parseLevelArr(lvl.originalGeo);

			lvl.originalTiles = lvl.readLevel
				.substring(lvl.readLevel.indexOf("#tlMatrix: [[[[#tp: ") + 11,
					lvl.readLevel.indexOf("]]]]", lvl.readLevel.indexOf("#tlMatrix: [[[[#tp: ") + 11) + 4);
			lvl.tiles = Level.parseLevelArr(lvl.originalTiles);

			lvl.originalProps = lvl.readLevel
				.substring(lvl.readLevel.indexOf("[#props: [[") + 9,
					lvl.readLevel.indexOf("]]]]", lvl.readLevel.indexOf("[#props: [[")) + 4);
			lvl.props = parseProps(lvl.originalProps);

		} catch (IOException e) {
			System.err.println("Error reading the file: " + e.getMessage());
		}

		return lvl;
	}

	public static String[] parseProps(String arrayson) {
		int depth = 0;
		int pidx = 0;

		List<String> entries = new ArrayList<String>();

		for (int i = 0; i < arrayson.length(); i++) {
			char c = arrayson.charAt(i);

			if (c == '[') {
				depth++;

				if (depth == 2) {
					pidx = i;
				}

			}

			if (c == ']') {
				depth--;

				if (depth == 1) {
					entries.add(arrayson.substring(pidx, i + 1));
				}

			}

		}

		return entries.toArray(new String[0]);
	}

	public static String[][][] parseLevelArr(String arrayson) {

		int depth = 0;

		boolean searchingWidth = false;
		boolean searchingHeight = false;

		int width = 0;
		int height = 0;

		int cidx = 0;
		List<String> entries = new ArrayList<String>();

		// Find Dimensions
		for (int i = 0; i < arrayson.length(); i++) {
			char c = arrayson.charAt(i);

			if (c == '[') {
				depth++;

				if (depth == 4) {
					cidx = i;
				}

				if (depth == 2) {

					if (!searchingHeight && height == 0) {
						searchingHeight = true;
					}

				}

				if (depth == 1) {

					if (!searchingWidth && width == 0) {
						searchingWidth = true;
					}

				}

			}

			if (c == ']') {
				depth--;

				if (depth == 3) {
					entries.add(arrayson.substring(cidx, i + 1));
				}

				if (searchingHeight) {

					if (depth == 2) {
						height++;
					}

					if (depth == 1) {
						searchingHeight = false;
					}

				}

				if (searchingWidth) {

					if (depth == 1) {
						width++;
					}

					if (depth == 0) {
						searchingWidth = false;
					}

				}

			}

		}

		String[][][] newLevel = new String[width][height][3];

		int index = 0;

		for (int r = 0; r < width; r++) {

			for (int c = 0; c < height; c++) {

				for (int d = 0; d < 3; d++) {
					newLevel[r][c][d] = entries.get(index++);
				}

			}

		}

		return newLevel;
	}

	public void cycle(boolean swapGeo, boolean swapTiles, boolean swapProps) {

		if (swapGeo) {

			for (String[][] c : geo) {

				for (String[] r : c) {

					String l = r[2];

					r[2] = r[1];
					r[1] = r[0];
					r[0] = l;
				}

			}

		}

		if (swapTiles) {

			for (String[][] c : tiles) {

				for (String[] r : c) {

					String l = r[2];

					r[2] = r[1];
					r[1] = r[0];
					r[0] = l;
				}

			}

		}

		if (swapProps) {

			for (int i = 0; i < props.length; i++) {
				String p = props[i];

				int d = Integer.parseInt(p.substring(1, p.indexOf(',')));
				d -= 10;
				d = d % 30;

				props[i] = "[" + Integer.toString(d) + p.substring(p.indexOf(','), p.length());
			}

		}

	}

	public void swap13(boolean swapGeo, boolean swapTiles, boolean swapProps) {

		if (swapGeo) {

			for (String[][] c : geo) {

				for (String[] r : c) {

					String temp = r[0];
					r[0] = r[r.length - 1];
					r[r.length - 1] = temp;

				}

			}

		}

		if (swapTiles) {

			for (String[][] c : tiles) {

				for (String[] r : c) {

					String temp = r[0];
					r[0] = r[2];
					r[2] = temp;

				}

			}

		}

		if (swapProps) {

			for (int i = 0; i < props.length; i++) {
				String p = props[i];

				int d = Integer.parseInt(p.substring(1, p.indexOf(',')));

				if (d > -10 && d < 0) {
					d -= 20;
				} else if (d > -30 && d <= -20) {
					d -= 10;
				}

				d = d % 30;

				props[i] = "[" + Integer.toString(d) + p.substring(p.indexOf(','), p.length());
			}

		}

	}

	public void swap12(boolean swapGeo, boolean swapTiles, boolean swapProps) {

		if (swapGeo) {

			for (String[][] c : geo) {

				for (String[] r : c) {

					String temp = r[0];
					r[0] = r[1];
					r[1] = temp;

				}

			}

		}

		if (swapTiles) {

			for (String[][] c : tiles) {

				for (String[] r : c) {

					String temp = r[0];
					r[0] = r[1];
					r[1] = temp;

				}

			}

		}

		if (swapProps) {

			for (int i = 0; i < props.length; i++) {
				String p = props[i];

				int d = Integer.parseInt(p.substring(1, p.indexOf(',')));

				if (d > -10 && d < 0) {
					d -= 10;
				} else if (d > -20 && d <= -10) {
					d -= 20;
				}

				d = d % 30;

				props[i] = "[" + Integer.toString(d) + p.substring(p.indexOf(','), p.length());
			}

		}

	}

	public void swap23(boolean swapGeo, boolean swapTiles, boolean swapProps) {

		if (swapGeo) {

			for (String[][] c : geo) {

				for (String[] r : c) {

					String temp = r[2];
					r[2] = r[1];
					r[1] = temp;

				}

			}

		}

		if (swapTiles) {

			for (String[][] c : tiles) {

				for (String[] r : c) {

					String temp = r[2];
					r[2] = r[1];
					r[1] = temp;

				}

			}

		}

		if (swapProps) {

			for (int i = 0; i < props.length; i++) {
				String p = props[i];

				int d = Integer.parseInt(p.substring(1, p.indexOf(',')));

				if (d > -20 && d < -10) {
					d -= 10;
				} else if (d > -30 && d <= -20) {
					d -= 20;
				}

				d = d % 30;

				props[i] = "[" + Integer.toString(d) + p.substring(p.indexOf(','), p.length());
			}

		}

	}

	public String arrToString(Object array) {
		StringBuilder sb = new StringBuilder();

		if (array instanceof Object[]) {
			sb.append("[");
			Object[] arr = (Object[]) array;

			for (int i = 0; i < arr.length; i++) {

				if (i > 0) {
					sb.append(",");
				}

				sb.append(arrToString(arr[i]));
			}

			sb.append("]");
		} else {
			sb.append(array);
		}

		return sb.toString();
	}

	public String save() {
		return this.readLevel
			.replace(originalGeo, arrToString(geo))
			.replace(originalTiles, arrToString(tiles))
			.replace(originalProps, arrToString(props));
	}

}
