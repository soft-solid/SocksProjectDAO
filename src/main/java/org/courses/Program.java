package org.courses;

import org.courses.DAO.Type;
import org.courses.commands.Command;
import org.courses.commands.CommandFormatException;
import org.courses.commands.jdbc.*;
import org.apache.tools.ant.types.Commandline;
import org.courses.domain.jdbc.BaseEntity;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Program {
    static Map<String, Command> commands;

    static {
        commands = new HashMap<>();
        commands.put("connect", new CreateDb());
        commands.put("table", new CreateTable());
        commands.put("addtype", new AddTypeCommand());
        commands.put("addmaterial", new AddMaterialCommand());
        commands.put("addmanufacture", new AddManufactureCommand());
        commands.put("listmaterial", new ListMaterialCommand());
        commands.put("listtype", new ListTypeCommand());
        commands.put("listmanufacture", new ListManufactureCommand());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection con = ConnectionManager.getConnection("CourseDB.db");

        String line = "";
        greetUser();
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            if (line.equals("exit"))
                break;
            //коректние бы выглядело пробежаться по всем аннотациям и так вывести тип. Но для этой реализации нужно доп. время, а его очень не хватат (spring вроде бы позволит это сделать с наименьшими усилиями)
            String userCommand[] = Commandline.translateCommandline(line);
            Object ins = null;
            Class classVar;

            //каждую опперацию тестить отдельно

//          //вызов Read() ----> Type 3
//
//            try {
//                classVar = Class.forName(String.format("org.courses.DAO.%s",userCommand[0]));
//                Constructor<?> ctor = classVar.getConstructor(Connection.class);
//                ins = ctor.newInstance(new Object[] { con });
//            }
//            catch (Exception e)
//            {
//                System.out.println(e);
//                break;
//            }
//            BaseEntity baseEntity = (BaseEntity) ins;
//            baseEntity.Read(Integer.parseInt(userCommand[1]));
//            Field[] fields = classVar.getDeclaredFields();
//            try {
//                for (Field field : fields){
//                    field.setAccessible(true);
//                    System.out.println(String.format("%s = %s",field.getName(),field.get(baseEntity)));
//                }
//            }
//            catch (Exception e){}


            /////////////////////////////////////////////////////////////////


//            //вызов Save() ----> Type SomeTypy1
//
//            try {
//                classVar = Class.forName(String.format("org.courses.DAO.%s", userCommand[0]));
//                Constructor<?> ctor = classVar.getConstructor(String.class, Connection.class);
//                ins = ctor.newInstance(new Object[]{userCommand[1], con});
//            } catch (Exception e) {
//                System.out.println(e);
//                break;
//            }
//            BaseEntity baseEntity = (BaseEntity) ins;
//            int res = baseEntity.Save();
//            System.out.println(res);
////
//            /////////////////////////////////////////////////////////////////

//            //вызов Delete() ----> Type 32
//            try {
//                classVar = Class.forName(String.format("org.courses.DAO.%s", userCommand[0]));
//                Constructor<?> ctor = classVar.getConstructor(Connection.class);
//                ins = ctor.newInstance(new Object[]{con});
//            } catch (Exception e) {
//                System.out.println(e);
//                break;
//            }
//            BaseEntity baseEntity = (BaseEntity) ins;
//            baseEntity.Delete(Integer.parseInt(userCommand[1]));

            ////////////////////////////////////////////////////////////////////
            greetUser();
        }
    }

    private static void parseUserInput(String input) {
        String userCommand[] = Commandline.translateCommandline(input);

        String commandName = userCommand[0];
        String[] params = new String[userCommand.length - 1];
        System.arraycopy(userCommand, 1, params, 0, params.length);

        try {
            executeCommand(commandName, params);
        } catch (CommandFormatException e) {
            System.out.println(String.format("%s has some invalid arguments", commandName));
        } catch (NotImplementedException e) {
            System.out.println(String.format("Unknown command %s", commandName));
        }
    }

    private static void executeCommand(String commandName, String[] params) {
        Command command = commands.get(commandName);

        if (null == command)
            throw new NotImplementedException();

        command.parse(params);
        command.execute();
    }

    private static void greetUser() {
        System.out.print("courses-jdbc>");
    }
}
