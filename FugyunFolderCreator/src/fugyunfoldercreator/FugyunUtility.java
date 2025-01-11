package fugyunfoldercreator;

import java.awt.Component;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

/**
 * ふぎゅんユーティリティ
 */
class FugyunUtility
{
	/**
	 * コンポーネント
	 */
	private Component parent;

	/**
	 * カスタムメッセージボックス
	 */
	private CustomMessageBox msgBox = new CustomMessageBox();

	/**
	 * ドライブレターリスト
	 */
	private final List<String> DRIVE_LETTER_LIST = Arrays.asList(
			"A:", "B:", "C:", "D:", "E:", "F:", "G:", "H:", "I:", "J:", "K:", "L:", "M:",
			"N:", "O:", "P:", "Q:", "R:", "S:", "T:", "U:", "V:", "W:", "X:", "Y:", "Z:");

	/**
	 * 使用不可フォルダ名リスト
	 */
	private final List<String> INVALID_FOLDER_NAME_LIST = Arrays.asList(
			"CON", "PRN", "AUX", "NUL",
			"COM0", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
			"LPT0", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9");

	/**
	 * 全角数値リスト
	 */
	private final List<String> FULL_WIDTH_NUMBER_LIST = Arrays.asList("０", "１", "２", "３", "４", "５", "６", "７", "８", "９");

	/**
	 * 使用不可文字リスト
	 */
	private final List<String> INVALID_CHAR_LIST = Arrays.asList("<", ">", ":", "\"", "/", "|", "?", "*");

	/**
	 * コンストラクタ
	 * @param parentComponent 親コンポーネント
	 */
	public FugyunUtility(Component parentComponent)
	{
		parent = parentComponent;
	}

	/**
	 * フォルダ作成処理
	 * @param pathText パステキスト
	 */
	public void FolderCreateTran(String pathText)
	{
		String path;
		String tempPath = "";

		try
		{
			int result = msgBox.showMessageBox(
					parent,
					"フォルダ作成確認",
					"入力されたパスでフォルダを作成してよろしいですか？",
					JOptionPane.QUESTION_MESSAGE,
					400,
					150,
					Const.BUTTON_TEXT_LIST_QUSTION,
					1);
			if (result == JOptionPane.YES_OPTION)
			{
				// 未入力チェック
				if (pathText.isBlank())
				{
					msgBox.showMessageBox(
							parent,
							Const.ERROR,
							"フォルダパスが未入力です。",
							JOptionPane.ERROR_MESSAGE,
							250,
							150,
							Const.BUTTON_TEXT_LIST_OK_ONLY,
							0);
					return;
				}

				List<String> driveLetterList = getDriveLetterList();
				List<String> createPathList = new ArrayList<>();

				String[] pathAr = pathText.split("\n", -1);
				String[] tempPathAr;

				// 入力されたパスを基に処理を行う。
				for (int index = 0; index < pathAr.length; index++)
				{
					// パスを保持する。
					path = pathAr[index];

					// パスの有無を判定する。
					if (!path.isBlank())
					{
						// ドライブ文字を、大文字に変換し、保持する。
						path = path.substring(0, 1).toUpperCase() + path.substring(1);

						// チェック処理を行い、結果を判定する。
						if (CheckTran(parent, path, driveLetterList, index + 1))
						{
							tempPathAr = path.split("\\\\");

							// 各フォルダ名が使用不可リストに含まれているかチェック
							for (String folderName : tempPathAr)
							{
								// フォルダ名を判定する。
								if (INVALID_FOLDER_NAME_LIST.contains(folderName))
								{
									msgBox.showMessageBox(
											parent,
											Const.ERROR,
											convertNumberWide(index + 1) + "行目：フォルダ名に使用出来ない文字列（予約語）が含まれています。",
											JOptionPane.ERROR_MESSAGE,
											600,
											150,
											Const.BUTTON_TEXT_LIST_OK_ONLY,
											0);
									return;
								}
							}

							// パスの有無を判定する。
							if (!createPathList.contains(path))
							{
								// 未設定のパスの場合
								createPathList.add(path);
							}
						}
						else
						{
							return;
						}
					}
				}

				int folderNo = 0;

				// 仮フォルダを作成する。
				boolean createFlg = false;

				do
				{
					// 仮フォルダパスを生成する。
					tempPath = driveLetterList.get(0) + "\\" + folderNo;

					// フォルダ検索処理を呼び出し、結果の有無を判定する。
					if (Files.exists(Paths.get(tempPath), LinkOption.NOFOLLOW_LINKS))
					{
						// 存在する場合
						folderNo++;
					}
					else
					{
						// 存在しない場合

						// 仮フォルダを生成し、作成フラグを設定する。
						Files.createDirectories(Paths.get(tempPath));
						createFlg = true;
					}
				} while (!createFlg);

				// 仮作成
				for (String createPath : createPathList)
				{
					// 作成するフォルダパスを生成し、フォルダを作成する。
					Files.createDirectories(Paths.get(tempPath + createPath.substring(2)));
				}

				// 仮フォルダを削除する。
				deleteDirectory(new File(tempPath));

				// 本作成
				for (String createPath : createPathList)
				{
					// 作成するフォルダパスを生成し、フォルダを作成する。
					Files.createDirectories(Paths.get(createPath));
				}

				// 完了メッセージボックスを表示する。
				msgBox.showMessageBox(
						parent,
						"フォルダ作成完了",
						"フォルダが作成されました。",
						JOptionPane.INFORMATION_MESSAGE,
						250,
						150,
						Const.BUTTON_TEXT_LIST_OK_ONLY,
						0);
			}
		}
		catch (Exception e)
		{
			// 仮フォルダのパスの保持の有無を判定する。
			if (!tempPath.isEmpty() && Files.exists(Paths.get(tempPath), LinkOption.NOFOLLOW_LINKS))
			{
				// 保持されている場合
				deleteDirectory(new File(tempPath));
			}

			showCatchErrorMessage(parent, e.getMessage());
		}
	}

	/**
	 * フォルダ削除処理
	 * @param allDeleteFlg 全削除フラグ（true：全削除・false：削除）
	 * @param pathText パステキスト
	 */
	public void FolderDeleteTran(boolean allDeleteFlg, String pathText)
	{
		String path;

		try
		{
			String title = "フォルダ削除確認";
			String message = "<html>入力されたパスのフォルダを削除してよろしいですか？<br>（フォルダ内にファイルが存在する場合、ファイルごと削除します。）";

			// 全削除フラグを判定する。
			if (allDeleteFlg)
			{
				// 全削除の場合
				title = "フォルダ全削除確認";
				message = "<html>入力されたパスの全フォルダを削除してよろしいですか？<br>（フォルダ内にファイルが存在する場合、ファイルごと削除します。）";
			}

			int result = msgBox.showMessageBox(
					parent,
					title,
					message,
					JOptionPane.QUESTION_MESSAGE,
					480,
					150,
					Const.BUTTON_TEXT_LIST_QUSTION,
					1);
			if (result == JOptionPane.YES_OPTION)
			{
				// 未入力チェック
				if (pathText.isBlank())
				{
					msgBox.showMessageBox(
							parent,
							Const.ERROR,
							"フォルダパスが未入力です。",
							JOptionPane.ERROR_MESSAGE,
							250,
							150,
							Const.BUTTON_TEXT_LIST_OK_ONLY,
							0);
					return;
				}

				List<String> driveLetterList = getDriveLetterList();
				List<String> deletePathList = new ArrayList<>();

				String[] pathAr = pathText.split("\n", -1);

				// 入力されたパスを基に処理を行う。
				for (int index = 0; index < pathAr.length; index++)
				{
					// パスを保持する。
					path = pathAr[index];

					// パスの有無を判定する。
					if (!path.isBlank())
					{
						// ドライブ文字を、大文字に変換し、保持する。
						path = path.substring(0, 1).toUpperCase() + path.substring(1);

						// チェック処理を行い、結果を判定する。
						if (CheckTran(parent, path, driveLetterList, index + 1))
						{
							// フォルダ検索処理を呼び出し、結果の有無を判定する。
							if (Files.exists(Paths.get(path), LinkOption.NOFOLLOW_LINKS))
							{
								// 存在する場合

								// 全削除フラグを判定する。
								if (allDeleteFlg)
								{
									// 全削除の場合
									path = Arrays.stream(path.split("\\\\")).limit(2).collect(Collectors.joining("\\"));
								}

								// パスの有無を判定する。
								if (!deletePathList.contains(path))
								{
									// 未設定のパスの場合
									deletePathList.add(path);
								}
							}
						}
						else
						{
							return;
						}
					}
				}

				// フォルダを削除する。
				for (String deletePath : deletePathList)
				{
					deleteDirectory(new File(deletePath));
				}

				// 全削除フラグを判定する。
				if (allDeleteFlg)
				{
					// 全削除の場合
					title = "フォルダ全削除完了";
					message = "フォルダが全削除されました。";
				}
				else
				{
					// 削除の場合
					title = "フォルダ削除完了";
					message = "フォルダが削除されました。";
				}

				// 完了メッセージボックスを表示する。
				msgBox.showMessageBox(
						parent,
						title,
						message,
						JOptionPane.INFORMATION_MESSAGE,
						270,
						150,
						Const.BUTTON_TEXT_LIST_OK_ONLY,
						0);
			}
		}
		catch (Exception e)
		{
			showCatchErrorMessage(parent, e.getMessage());
		}
	}

	/**
	 * チェック処理
	 * @param parent 親コンポーネント
	 * @param path パス
	 * @param driveLetterList ドライブ文字リスト
	 * @param rowIndex 行数
	 * @return チェック結果（true：正常・false：エラー）
	 */
	private boolean CheckTran(Component parent, String path, List<String> driveLetterList, int rowIndex)
	{
		// 文字数チェック
		if (240 < path.length())
		{
			msgBox.showMessageBox(
					parent,
					Const.ERROR,
					"<html>" + convertNumberWide(rowIndex) + "行目：パスの入力可能文字数は、２４０文字までです。<br>入力文字数：" + convertNumberWide(path.length()) + "文字",
					JOptionPane.ERROR_MESSAGE,
					500,
					150,
					Const.BUTTON_TEXT_LIST_OK_ONLY,
					0);
			return false;
		}

		// 無名フォルダチェック
		if (path.indexOf("\\\\") != -1)
		{
			msgBox.showMessageBox(
					parent,
					Const.ERROR,
					convertNumberWide(rowIndex) + "行目：フォルダ名が指定されていない箇所があります。",
					JOptionPane.ERROR_MESSAGE,
					500,
					150,
					Const.BUTTON_TEXT_LIST_OK_ONLY,
					0);
			return false;
		}

		// パスを配列化する。
		// （末端要素がブランクの場合、削除する。（スペースの場合は削除しない。））
		String[] folderNameAr = path.split("\\\\", -1);
		if (folderNameAr[folderNameAr.length - 1].isEmpty())
		{
			folderNameAr = Arrays.copyOf(folderNameAr, folderNameAr.length - 1);
		}

		// 要素数チェック
		if (2 > folderNameAr.length)
		{
			msgBox.showMessageBox(
					parent,
					Const.ERROR,
					convertNumberWide(rowIndex) + "行目：フォルダ名が入力されていません。",
					JOptionPane.ERROR_MESSAGE,
					500,
					150,
					Const.BUTTON_TEXT_LIST_OK_ONLY,
					0);
			return false;
		}

		// パス使用文字チェック（ドライブ文字以外でループさせる。）
		for (int index = 1; index < folderNameAr.length; index++)
		{
			String folderName = folderNameAr[index];
			for (String invalidChar : INVALID_CHAR_LIST)
			{
				if (folderName.contains(invalidChar))
				{
					// パスに使用出来ない文字が使用されている場合、エラーとする。
					msgBox.showMessageBox(
							parent,
							Const.ERROR,
							convertNumberWide(rowIndex) + "行目：フォルダ名に使用出来ない文字が含まれています。",
							JOptionPane.ERROR_MESSAGE,
							500,
							150,
							Const.BUTTON_TEXT_LIST_OK_ONLY,
							0);
					return false;
				}
			}
		}

		// スペース・ドットチェック
		for (String folderName : folderNameAr)
		{
			if (folderName.isBlank())
			{
				// スペースのみのフォルダが存在する場合、エラーとする。
				msgBox.showMessageBox(
						parent,
						Const.ERROR,
						convertNumberWide(rowIndex) + "行目：スペース（全角・半角問わず）のみのフォルダは、処理出来ません。",
						JOptionPane.ERROR_MESSAGE,
						600,
						150,
						Const.BUTTON_TEXT_LIST_OK_ONLY,
						0);
				return false;
			}
			else if (".".equals(folderName))
			{
				// ドットのみのフォルダが存在する場合、エラーとする。
				msgBox.showMessageBox(
						parent,
						Const.ERROR,
						convertNumberWide(rowIndex) + "行目：【.】のみのフォルダは、処理出来ません。",
						JOptionPane.ERROR_MESSAGE,
						500,
						150,
						Const.BUTTON_TEXT_LIST_OK_ONLY,
						0);
				return false;
			}
			else if (folderName.endsWith("."))
			{
				// フォルダ名の末尾が【.】の場合、エラーとする。
				msgBox.showMessageBox(
						parent,
						Const.ERROR,
						convertNumberWide(rowIndex) + "行目：フォルダ名の末尾が【.】の場合、処理出来ません。",
						JOptionPane.ERROR_MESSAGE,
						500,
						150,
						Const.BUTTON_TEXT_LIST_OK_ONLY,
						0);
				return false;
			}
		}

		// ドライブチェック
		if (!driveLetterList.contains(folderNameAr[0]))
		{
			// 存在していないドライブ文字が指定されている場合、エラーとする。
			msgBox.showMessageBox(
					parent,
					Const.ERROR,
					convertNumberWide(rowIndex) + "行目：存在しないドライブ文字（C:やD:）が入力されています。",
					JOptionPane.ERROR_MESSAGE,
					500,
					150,
					Const.BUTTON_TEXT_LIST_OK_ONLY,
					0);
			return false;
		}

		return true;
	}

	/**
	 * ドライブ文字リスト取得処理
	 * @return ドライブ文字リスト
	 */
	public List<String> getDriveLetterList()
	{
		List<String> driveLetterList = new ArrayList<>();

		// ルートディレクトリ一覧を取得し、ループさせる。
		File[] roots = File.listRoots();
		for (File root : roots)
		{
			String driveLetter = root.getPath().replace("\\", "");

			// 使用可能なドライブタイプに含まれているか確認し、リストに追加する。
			if (DRIVE_LETTER_LIST.contains(driveLetter) && root.canRead())
			{
				driveLetterList.add(driveLetter);
			}
		}

		return driveLetterList;
	}

	/**
	 * 半角数値→全角数値変換処理
	 *
	 * @param narrowNumber 半角数値
	 * @return 変換した全角数値
	 */
	private String convertNumberWide(int narrowNumber)
	{
		String narrowNumberStr = String.valueOf(narrowNumber);
		StringBuilder convertNumber = new StringBuilder();

		for (char number : narrowNumberStr.toCharArray())
		{
			int index = Character.getNumericValue(number);
			if (index >= 0 && index < FULL_WIDTH_NUMBER_LIST.size())
			{
				convertNumber.append(FULL_WIDTH_NUMBER_LIST.get(index));
			}
		}

		return convertNumber.toString();
	}

	/**
	 * フォルダ削除処理
	 * （フォルダ内に読み取り専用ファイルが存在しても、強制削除する。）
	 * @param path パス
	 * @return 削除結果（true：削除・false：削除エラー）
	 */
	public boolean deleteDirectory(File path)
	{
		// フォルダが存在し、かつフォルダか判定する。
		if (path.exists() && path.isDirectory())
		{
			// フォルダ内の全ファイル・サブフォルダを取得する。
			File[] files = path.listFiles();
			if (files != null)
			{
				for (File file : files)
				{
					// ファイルを判定する。
					if (file.isDirectory())
					{
						// フォルダの場合、再帰処理を行う。
						deleteDirectory(file);
					}
					else
					{
						// ファイルの場合、【編集】にし、削除する。
						file.setWritable(true);
						file.delete();
					}
				}
			}
		}

		// 指定されたフォルダを削除する。
		return path.delete();
	}

	/**
	 * キャッチエラーメッセージ表示処理
	 * @param parent
	 * @param message
	 */
	public void showCatchErrorMessage(
			Component parent,
			String message)
	{
		new CustomMessageBox().showMessageBox(
				parent,
				Const.ERROR,
				message,
				JOptionPane.ERROR_MESSAGE,
				600,
				150,
				Const.BUTTON_TEXT_LIST_OK_ONLY,
				0);
	}
}