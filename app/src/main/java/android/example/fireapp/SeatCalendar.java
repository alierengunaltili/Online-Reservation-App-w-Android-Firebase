package android.example.fireapp;

import android.os.Build;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class SeatCalendar extends HashMap<String, TimeSlot> {

    // properties
    LocalTime availableHoursStart;
    LocalTime availableHoursEnd;
    LocalDate currentDate;

    // constructors
    public SeatCalendar(LocalDate date, LocalTime start, LocalTime end) {

        currentDate = date;
        availableHoursEnd = end;

        if( date.isEqual(LocalDate.now()))
        {
            LocalTime i;
            for ( i = start; i.isBefore(LocalTime.now()); i = i.plusMinutes(10))
            {
                // do nothing
                System.out.println("eğer girilen tarih eşitse buraya giriyorum - time: " + i);
            }
            availableHoursStart = i;
        }
        else
        {
            availableHoursStart = start;
        }
        createTimeSlots( availableHoursStart, getLastReservationTime());
        System.out.println("Bir sıkıntı yokk");
    }

    // methods
    private void createTimeSlots( LocalTime start, LocalTime end)
    {
        System.out.println("Son rezervasyon zamanını da sıkıntısız veriyorum  " + end);
        TimeSlot ts;

        for( LocalTime i = start; i.isBefore(end); i = i.plusMinutes(10)) // 10 represent intervals, we can change if needed
        {
            ts = new TimeSlot( currentDate, i);
            ts.setReserved(false);
            this.put("" + (i.getHour()*60 + i.getMinute()), ts);
            System.out.println("zaman aralıklarını oluşturuyorum :))   " + ts.toString() );
        }

        System.out.println("Zaman aralıklarını oluşturdum");
		/*
		String[] endHourMinute = end.split(":");

		int startHour = Integer.parseInt(startHourMinute[0]);
		int startMinute = Integer.parseInt(startHourMinute[1]);

		int endHour = Integer.parseInt(endHourMinute[0]);
		int endMinute = Integer.parseInt(endHourMinute[1]);

		System.out.println(startHour + " " + startMinute + " " + endHour + " " + endMinute);
		timeSlots = new ArrayList<TimeSlot>();

		for( int i = startHour*60 + startMinute; i < endHour*60 + endMinute - TimeSlot.durationOfMeal; i++)
		{
			TimeSlot ts = new TimeSlot(currentDate, i);
			ts.setReserved(false);
			timeSlots.add(ts);
			System.out.println(ts.toString());
		}
		*/
    }

//    public boolean isTimeSlotAvailable(LocalTime time) {
//        return !this.get(time.getHour()*60 + time.getMinute()).isReserved();
//    }
//
//    public void setRelatedSlotsReserved(LocalDateTime dateAndTime, boolean b) {
//        for ( int i = dateAndTime.getHour()*60 + dateAndTime.getMinute();
//              (i < getLastReservationTime().getHour()*60 + getLastReservationTime().getMinute() + 1) && (i < dateAndTime.getHour()*60 + dateAndTime.getMinute() + TimeSlot.durationOfMeal);
//              i++)
//        {
//            this.get(i).setReserved(b);
//        }
//
//
//		/*int startIndex = timeSlots.indexOf( getTimeSlotByStartTime(ts.getFirstTime()));
//		System.out.println("start Index: " + startIndex );
//		for(int i = startIndex; i < startIndex + TimeSlot.durationOfMeal - 1; i++)
//		{
//			timeSlots.get(i).setReserved(b);
//		}
//		System.out.println("Related timeslots reserved");
//		*/
//    }

//    public String printReservedTimeSlots()
//    {
//        String str = "";
//
//        for( int i = availableHoursStart.getHour()*60 + availableHoursStart.getMinute(); i < getLastReservationTime().getHour()*60 + getLastReservationTime().getMinute(); i++ )
//        {
//            if( this.get(i).isReserved())
//            {
//                str += this.get(i);
//                str += "\n";
//                i = i + TimeSlot.durationOfMeal;
//            }
//        }
//        return str;
//
//		/*
//		for( Integer key : timeSlots.keySet())
//		{
//			if( timeSlots.get(key).isReserved())
//			{
//				str += timeSlots.get(key);
//				str += "\n";
//			}
//		}
//		return str;
//		*/
//    }

    /*public TimeSlot getTimeSlotByStartTime(int time)
    {
        TimeSlot ts;
        for( int i = 0; i < timeSlots.size(); i++)
        {
            if(timeSlots.get(i).getFirstTime() == time)
            {
                ts = timeSlots.get(i);
                return ts;
            }
        }
        return null;
    }
    */
    public LocalTime getLastReservationTime() {
        // TODO Auto-generated method stub
        return availableHoursEnd.minusMinutes(TimeSlot.durationOfMeal);

    }
}
