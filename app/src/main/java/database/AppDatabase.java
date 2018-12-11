package database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

import dao.PointDao;
import dao.PointTaskDao;
import dao.TaskDao;
import dao.MemberDao;
import entities.Member;
import entities.Point;
import entities.Task;

@Database(entities = { Point.class, Task.class, Member.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract PointDao pointDao();
    public abstract TaskDao taskDao();
    public abstract PointTaskDao pointTaskDao();
    public abstract MemberDao memberDao();

    private static ArrayList<Point> getPoints() {
        ArrayList<Point> points = new ArrayList<>();

        Point p1 = new Point();
        p1.setTitle("Woodbine Beach");
        p1.setAddress("1675 Lake Shore Blvd E, Toronto, ON");
        p1.setLongitude(-79.311180);
        p1.setLatitude(43.661550);
        p1.setTag("Outdoors");
        p1.setRating(5);

        Point p2 = new Point();
        p2.setTitle("Trinity Bellwoods Park");
        p2.setAddress("790 Queen St W, Toronto, ON");
        p2.setLongitude(-79.413210);
        p2.setLatitude(43.645340);
        p2.setTag("Park");
        p2.setRating(4);

        Point p3 = new Point();
        p3.setTitle("Allan Gardens");
        p3.setAddress("160 Gerrard St E, Toronto, ON");
        p3.setLongitude(-79.371430);
        p3.setLatitude(43.661410);
        p3.setTag("Walkable");
        p3.setRating(2);

        Point p4 = new Point();
        p4.setTitle("Sky Zone Trampoline Park");
        p4.setAddress("45 Esandar Dr Unit 1A, Toronto, ON");
        p4.setLongitude(-79.359170);
        p4.setLatitude(43.705630);
        p4.setTag("Indoors");
        p4.setRating(5);

        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);

        return points;
    }

    private static ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        Task t1 = new Task();
        t1.setTitle("Take a picture");
        t1.setDescription("Take a picture with as many people in it as you can!");
        t1.setAchieved(false);

        Task t2 = new Task();
        t2.setTitle("Karate expert");
        t2.setDescription("Start performing kung fu moves against an invisible opponent!");
        t2.setAchieved(true);

        Task t3 = new Task();
        t3.setTitle("Talk to a stranger");
        t3.setDescription("Try interacting with somebody you don't know!");
        t3.setAchieved(true);

        Task t4 = new Task();
        t4.setTitle("Find the tallest tree");
        t4.setDescription("Take a picture of the tallest tree around you!");
        t4.setAchieved(false);

        Task t5 = new Task();
        t5.setTitle("Do 10 pushups");
        t5.setDescription("Drop to the floor and do 10 pushups!");
        t5.setAchieved(true);

        Task t6 = new Task();
        t6.setTitle("Bless you");
        t6.setDescription("Get a stranger to say \"Bless you\" ");
        t6.setAchieved(false);

        tasks.add(t1);
        tasks.add(t2);
        tasks.add(t3);
        tasks.add(t4);
        tasks.add(t5);
        tasks.add(t6);

        for(Task task : tasks) {
            Random rand = new Random();
            task.setPointId(rand.nextInt(4) + 1);
        }

        return tasks;
    }

    private static ArrayList<Member> getMembers() {
        ArrayList<Member> members = new ArrayList<>();

        Member m1 = new Member();
        m1.setName("John Smith");
        m1.setPhoneNumber("6476686823");
        m1.setEmail("john.smith@gmail.com");
        m1.setImageId(3);

        Member m2 = new Member();
        m2.setName("Antonio Zelofonia");
        m2.setPhoneNumber("4168350932");
        m2.setEmail("anton.z@gmail.com");
        m2.setImageId(5);

        Member m3 = new Member();
        m3.setName("Ashley White");
        m3.setPhoneNumber("2895974719");
        m3.setEmail("ashley.white@gmail.com");
        m3.setImageId(1);

        Member m4 = new Member();
        m4.setName("Jake Appletree");
        m4.setPhoneNumber("6476386812");
        m4.setEmail("jake.appletree@gmail.com");
        m4.setImageId(2);

        members.add(m1);
        members.add(m2);
        members.add(m3);
        members.add(m4);

        return members;
    }

    public static synchronized AppDatabase getAppDatabase(Context context) {
        if(INSTANCE == null) {

            final ArrayList<Point> points = getPoints();
            final ArrayList<Task> tasks = getTasks();
            final ArrayList<Member> members = getMembers();

            Callback rcb = new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    class PopulateDatabase extends AsyncTask<Void, Void, Integer> {
                        @Override
                        protected Integer doInBackground(Void... params) {
                            Executors.newSingleThreadExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    for(Point point : points) {
                                        INSTANCE.pointDao().insertOne(point);
                                    }

                                    for(Task task : tasks) {
                                        INSTANCE.taskDao().insertOne(task);
                                    }

                                    for(Member member : members) {
                                        INSTANCE.memberDao().insertOne(member);
                                    }
                                }
                            });

                            return 1;
                        }
                    }

                    new PopulateDatabase().execute();

                }
            };


            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "chase-database")
                    .addCallback(rcb)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
