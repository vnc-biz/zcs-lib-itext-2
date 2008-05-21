package classroom.festivaldatabase;

// Generated 18-mrt-2008 12:18:58 by Hibernate Tools 3.2.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * FilmTitle generated by hbm2java
 */
public class FilmTitle implements java.io.Serializable {

	private int filmId;
	private String title;
	private String ATitle;
	private String imdb;
	private int year;
	private int duration;
	private Set festivalScreenings = new HashSet(0);
	private Set festivalEntries = new HashSet(0);
	private Set directorNames = new HashSet(0);

	public FilmTitle() {
	}

	public FilmTitle(int filmId, String title, String imdb, int year,
			int duration) {
		this.filmId = filmId;
		this.title = title;
		this.imdb = imdb;
		this.year = year;
		this.duration = duration;
	}

	public FilmTitle(int filmId, String title, String ATitle, String imdb,
			int year, int duration, Set festivalScreenings,
			Set festivalEntries, Set directorNames) {
		this.filmId = filmId;
		this.title = title;
		this.ATitle = ATitle;
		this.imdb = imdb;
		this.year = year;
		this.duration = duration;
		this.festivalScreenings = festivalScreenings;
		this.festivalEntries = festivalEntries;
		this.directorNames = directorNames;
	}

	public int getFilmId() {
		return this.filmId;
	}

	public void setFilmId(int filmId) {
		this.filmId = filmId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getATitle() {
		return this.ATitle;
	}

	public void setATitle(String ATitle) {
		this.ATitle = ATitle;
	}

	public String getImdb() {
		return this.imdb;
	}

	public void setImdb(String imdb) {
		this.imdb = imdb;
	}

	public int getYear() {
		return this.year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getDuration() {
		return this.duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Set getFestivalScreenings() {
		return this.festivalScreenings;
	}

	public void setFestivalScreenings(Set festivalScreenings) {
		this.festivalScreenings = festivalScreenings;
	}

	public Set getFestivalEntries() {
		return this.festivalEntries;
	}

	public void setFestivalEntries(Set festivalEntries) {
		this.festivalEntries = festivalEntries;
	}

	public Set getDirectorNames() {
		return this.directorNames;
	}

	public void setDirectorNames(Set directorNames) {
		this.directorNames = directorNames;
	}

}
