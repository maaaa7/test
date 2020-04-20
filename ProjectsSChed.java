import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ProjectsSChed {

	public static void main(String[] args) {
		createPPSched();
	}

	private static final Scanner scanner = new Scanner(System.in);

	public static void createPPSched() {

		int tempProjectID = 0;
		String tempStartDate = null;
		String tempEndDate = null;
		String dependency = null;

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
		String refDate = null;

		int target = 0;

		try {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			tempStartDate = dateFormatter.format(cal.getTime());

			System.out.println("Please enter Project Name:");

			String n = scanner.nextLine();

			tempProjectID = tempProjectID + 1;

			System.out.println("Please enter no of tasks:");

			int noOfTasks = scanner.nextInt();
			scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

			if (noOfTasks == 0) {
				System.out.println("No tasks to be created.");
			} else {
				List<ProjectTasks> projectTaskList = new ArrayList<ProjectTasks>();
				for (int x = 0; noOfTasks > x; x++) {
					int y = 0;
					y = x + 1;
					refDate = null;

					System.out.println("Please enter task #" + y+":");
					String taskName = scanner.nextLine();

					System.out.println("Please enter target duration in days.");
					int duration = scanner.nextInt();
					scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

					if (!projectTaskList.isEmpty()) {

						System.out
								.println("Please enter dependencies." +
										"\n(Input task id here separated by comma if multiple dependencies " +
										"otherwise press enter.)");
						dependency = scanner.nextLine();

						if (!dependency.equalsIgnoreCase("")) {
							String[] dependencies = dependency.split(",");

							int dLength = dependencies.length;
							//System.out.println(dLength);

							if (dLength > 1) {

								for (int i = 0; i < dLength; i++) {
									int j = i + 1;
									if (j >= dLength) {
										break;
									}

									Calendar cal3 = Calendar.getInstance();
									cal3.setTime(dateFormatter.parse(projectTaskList
											.get(Integer
													.parseInt(dependencies[i]) - 1)
											.getEndDate()));
									Calendar cal4 = Calendar.getInstance();
									cal4.setTime(dateFormatter.parse(projectTaskList
											.get(Integer
													.parseInt(dependencies[j]) - 1)
											.getEndDate()));
									int result = cal3.compareTo(cal4);
									//System.out.println("result:" + result);
									if (result > 0) {
										
										target = Integer
												.parseInt(dependencies[i]);
									} else {

										target = Integer
												.parseInt(dependencies[i]) + 1;
									}

								}
							} else {

								target = Integer.parseInt(dependencies[0]);
							}
							//System.out.println("target is:" + target);
							refDate = projectTaskList.get(target - 1)
									.getEndDate();
							//System.out.println("refdate:" + refDate);
						}
					}

					if (refDate != null) {
						Calendar cal2 = Calendar.getInstance();
						cal2.setTime(dateFormatter.parse(refDate));
						cal2.add(Calendar.DAY_OF_YEAR, 1);
						tempStartDate = dateFormatter.format(cal2.getTime());
						cal2.add(Calendar.DAY_OF_YEAR,
								(int) Math.round(duration) - 1);
						//System.out.println(dateFormatter.format(cal2.getTime()));
						tempEndDate = dateFormatter.format(cal2.getTime());
					} else {
						cal.setTime(dateFormatter.parse(tempStartDate));
						cal.add(Calendar.DAY_OF_YEAR,
								(int) Math.round(duration) - 1);
						tempEndDate = dateFormatter.format(cal.getTime());
					}

					ProjectTasks projectTasks = new ProjectTasks();
					projectTasks.setTaskID(y);
					projectTasks.setTaskName(taskName);
					projectTasks.setStartDate(tempStartDate);
					projectTasks.setDuration(duration);
					projectTasks.setDependencies(dependency);
					projectTasks.setEndDate(tempEndDate);
					projectTaskList.add(projectTasks);
					System.out.println("Added: " + taskName + ". Task ID is "
							+ y+".");
				}
				System.out.println("Schedule for Project "+n+":");
				System.out.println(LPad("TASK ID:", 11, ' ')
						+ LPad("TASK NAME:", 20, ' ')
						+ LPad("DEPENDENCIES:", 17, ' ')
						+ LPad("START DATE:", 20, ' ')
						+ LPad("END DATE:", 20, ' ')
						+ LPad("DURATION:", 10, ' '));
				System.out.println("");
				for (ProjectTasks a : projectTaskList) {
					System.out.println(LPad(String.valueOf(a.getTaskID()), 11,
							' ')
							+ LPad(a.getTaskName(), 20, ' ')
							+ LPad(a.getDependencies(), 17, ' ')
							+ LPad(a.getStartDate(), 20, ' ')
							+ LPad(a.getEndDate(), 20, ' ')
							+ LPad(String.valueOf(a.getDuration()), 10, ' '));
				}
			}

		} catch (InputMismatchException ie) {
			System.out.println("Invalid input!");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Invalid Task ID for dependecies.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String LPad(String str, Integer length, char x) {

		if (str == null) {
			str = "";
		}
		return str
				+ String.format("%" + (length - str.length()) + "s", "")
						.replace(" ", String.valueOf(x));

	}

}
