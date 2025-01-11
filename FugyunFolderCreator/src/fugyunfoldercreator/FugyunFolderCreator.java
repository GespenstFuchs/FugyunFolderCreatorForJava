package fugyunfoldercreator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

/**
 * ふぎゅんフォルダ作成
 */
public class FugyunFolderCreator extends JFrame
{
	/**
	 * ふぎゅんユーティリティ
	 */
	private FugyunUtility utility;

	/**
	 * パステキストエリア
	 */
	private JTextArea pathTextArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FugyunFolderCreator frame = new FugyunFolderCreator();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FugyunFolderCreator()
	{
		utility = new FugyunUtility(this);
		UndoManager undoManager = new UndoManager();

		// ウィンドウを設定する。
		setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
		setTitle("ふぎゅんフォルダ作成");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1100, 600);

		// ウィンドウを中央に表示する。
		setLocationRelativeTo(null);

		// アイコンをリソースから読み込み、設定する。
		ImageIcon icon = new ImageIcon(getClass().getResource("/Icon.png"));
		setIconImage(icon.getImage());

		// メインパネル
		JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// 上部パネルを設定する。
		JPanel topPanel = new JPanel(new BorderLayout(0, 10));
		JPanel labelPanel = new JPanel(new GridLayout(4, 1, 0, 10));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

		// ラベルを追加する。
		JLabel label1 = new JLabel("フォルダを作成したい場合、フォルダパス入力後、【フォルダ作成】ボタンを押下して下さい。");
		label1.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		labelPanel.add(label1);
		JLabel label2 = new JLabel("フォルダを削除したい場合、フォルダパス入力後、【フォルダ削除】ボタンを押下して下さい。");
		label2.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		labelPanel.add(label2);
		JLabel label3 = new JLabel("複数のフォルダを削除したい場合、フォルダパス入力後、【フォルダ全削除】ボタンを押下して下さい。");
		label3.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		labelPanel.add(label3);
		JLabel label4 = new JLabel("１行最大２４０文字がパスとして入力可能で、１パスとします。（改行することで、複数のパスを入力出来ます。）");
		label4.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		labelPanel.add(label4);

		// ショートカット用アクション
		// フォルダ作成
		Action folderCreateAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				utility.FolderCreateTran(pathTextArea.getText());
				pathTextArea.requestFocusInWindow();
			}
		};

		// フォルダ削除
		Action folderDeleteAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				utility.FolderDeleteTran(false, pathTextArea.getText());
				pathTextArea.requestFocusInWindow();
			}
		};

		// フォルダ全削除
		Action folderAllDeleteAction = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				utility.FolderDeleteTran(true, pathTextArea.getText());
				pathTextArea.requestFocusInWindow();
			}
		};

		// 入力したパスをクリア
		Action pathClearAction = new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				pathTextArea.setText("");
				pathTextArea.requestFocusInWindow();
			}
		};

		// ボタンを追加する。
		// フォルダ作成ボタン
		JButton folderCreateButton = new JButton("<html><center>フォルダ作成<br>（Ｆ１）</center></html>");
		folderCreateButton.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));
		folderCreateButton.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					utility.FolderCreateTran(pathTextArea.getText());
					pathTextArea.requestFocusInWindow();
				}
			}
		});
		folderCreateButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				utility.FolderCreateTran(pathTextArea.getText());
				pathTextArea.requestFocusInWindow();
			}
		});
		folderCreateButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "folderCreate");
		folderCreateButton.getActionMap().put("folderCreate", folderCreateAction);
		buttonPanel.add(folderCreateButton);

		// フォルダ削除ボタン
		JButton folderDeleteButton = new JButton("<html><center>フォルダ削除<br>（Ｆ２）</center></html>");
		folderDeleteButton.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));
		folderDeleteButton.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					utility.FolderDeleteTran(false, pathTextArea.getText());
					pathTextArea.requestFocusInWindow();
				}
			}
		});
		folderDeleteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				utility.FolderDeleteTran(false, pathTextArea.getText());
				pathTextArea.requestFocusInWindow();
			}
		});
		folderDeleteButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "folderDelete");
		folderDeleteButton.getActionMap().put("folderDelete", folderDeleteAction);
		buttonPanel.add(folderDeleteButton);

		// フォルダ全削除ボタン
		JButton folderAllDeleteButton = new JButton("<html><center>フォルダ全削除<br>（Ｆ３）</center></html>");
		folderAllDeleteButton.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));
		folderAllDeleteButton.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					utility.FolderDeleteTran(true, pathTextArea.getText());
					pathTextArea.requestFocusInWindow();
				}
			}
		});
		folderAllDeleteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				utility.FolderDeleteTran(true, pathTextArea.getText());
				pathTextArea.requestFocusInWindow();
			}
		});
		folderAllDeleteButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "folderAllDelete");
		folderAllDeleteButton.getActionMap().put("folderAllDelete", folderAllDeleteAction);
		buttonPanel.add(folderAllDeleteButton);

		// 入力したパスをクリアボタン
		JButton pathClearButton = new JButton("<html><center>入力したパスをクリア<br>（Ｆ４）</center></html>");
		pathClearButton.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));
		pathClearButton.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					// テキストをクリアし、パステキストエリアにフォーカスを設定する。
					pathTextArea.setText("");
					pathTextArea.requestFocusInWindow();
				}
			}
		});
		pathClearButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// テキストをクリアし、パステキストエリアにフォーカスを設定する。
				pathTextArea.setText("");
				pathTextArea.requestFocusInWindow();
			}
		});
		pathClearButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "pathClear");
		pathClearButton.getActionMap().put("pathClear", pathClearAction);
		buttonPanel.add(pathClearButton);

		// ラベルパネルを上部、ボタンパネルを下部に追加する。
		topPanel.add(labelPanel, BorderLayout.NORTH);
		topPanel.add(buttonPanel, BorderLayout.SOUTH);

		// 下部パネル
		pathTextArea = new JTextArea();
		pathTextArea.setBackground(new Color(230, 230, 250));
		pathTextArea.setFont(new Font("Yu Gothic UI", Font.PLAIN, 24));
		pathTextArea.getDocument().addUndoableEditListener(new UndoableEditListener()
		{
			@Override
			public void undoableEditHappened(UndoableEditEvent e)
			{
				undoManager.addEdit(e.getEdit());
			}
		});
		pathTextArea.setLineWrap(false);
		pathTextArea.setWrapStyleWord(true);

		// テキストエリア・Tabキー遷移設定処理を呼び出す。
		setTabFocusTraversal(pathTextArea);

		JScrollPane scrollPane = new JScrollPane(pathTextArea);

		// 常にスクロールバーを表示するように設定する。
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// 上部パネル・下部パネルをメインパネルに追加する。
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		// フレームにメインパネルを追加する。
		getContentPane().add(mainPanel);
		setVisible(true);

		// ドロップターゲットを設定する。
		new DropTarget(this, new FolderDropTargetListener(this));
		new DropTarget(pathTextArea, new FolderDropTargetListener(this));

		// コンテキストメニューを生成する。
		JPopupMenu contextMenu = new JPopupMenu();

		// ショートカットキーテキストのフォントを設定する。
		UIManager.put("MenuItem.acceleratorFont", new Font("Yu Gothic UI", Font.PLAIN, 20));

		// ショートカットキーでの【元に戻す】・【やり直し】を設定する。
		InputMap inputMap = pathTextArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = pathTextArea.getActionMap();

		// 元に戻す
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "undo");
		actionMap.put("undo", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (undoManager.canUndo())
				{
					undoManager.undo();
				}
			}
		});

		// やり直し
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "redo");
		actionMap.put("redo", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (undoManager.canRedo())
				{
					undoManager.redo();
				}
			}
		});

		// 行選択
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK), "selectRow");
		actionMap.put("selectRow", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				selectLine();
			}
		});

		// 選択行のフォルダを開く
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK), "openFolder");
		actionMap.put("openFolder", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				openFolderAction(mainPanel);
			}
		});

		// 各メニュー項目を設定する。
		// 元に戻す
		JMenuItem undoItem = new JMenuItem("元に戻す");
		undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
		undoItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (undoManager.canUndo())
				{
					undoManager.undo();
				}
			}
		});
		undoItem.setFont(new Font("Yu Gothic UI", Font.PLAIN, 20));
		contextMenu.add(undoItem);

		// やり直し
		JMenuItem redoItem = new JMenuItem("やり直し");
		redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
		redoItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (undoManager.canRedo())
				{
					undoManager.redo();
				}
			}
		});
		redoItem.setFont(new Font("Yu Gothic UI", Font.PLAIN, 20));
		contextMenu.add(redoItem);

		// セパレータ
		contextMenu.addSeparator();

		// 切り取り
		JMenuItem cutItem = new JMenuItem("切り取り");
		cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
		cutItem.addActionListener(e -> pathTextArea.cut());
		cutItem.setFont(new Font("Yu Gothic UI", Font.PLAIN, 20));
		contextMenu.add(cutItem);

		// コピー
		JMenuItem copyItem = new JMenuItem("コピー");
		copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
		copyItem.addActionListener(e -> pathTextArea.copy());
		copyItem.setFont(new Font("Yu Gothic UI", Font.PLAIN, 20));
		contextMenu.add(copyItem);

		// 貼り付け
		JMenuItem pasteItem = new JMenuItem("貼り付け");
		pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
		pasteItem.addActionListener(e -> pathTextArea.paste());
		pasteItem.setFont(new Font("Yu Gothic UI", Font.PLAIN, 20));
		contextMenu.add(pasteItem);

		// 削除
		JMenuItem deleteItem = new JMenuItem("削除");
		deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		deleteItem.addActionListener(e ->
		{
			String text = pathTextArea.getText();

			if (text.isEmpty())
			{
				return;
			}

			int selectionStart = pathTextArea.getSelectionStart();
			int selectionEnd = pathTextArea.getSelectionEnd();

			// 選択範囲を判定する。
			if (selectionStart == selectionEnd)
			{
				// 存在しない場合
				if (selectionStart < text.length())
				{
					int newLineLength = System.lineSeparator().length();
					int removeLength = 1;

					// 選択開始位置＋改行コード数・文字数を判定する。
					if (selectionStart + newLineLength <= text.length())
					{
						if (text.substring(selectionStart, selectionStart + newLineLength).equals(System.lineSeparator()))
						{
							// 改行コードを削除する。
							removeLength = newLineLength;
						}
					}

					try
					{
						// 元に戻す・やり直しのため、ドキュメントを取得し、設定する。
						Document doc = pathTextArea.getDocument();
						doc.remove(selectionStart, removeLength);
					}
					catch (Exception ex)
					{
						utility.showCatchErrorMessage(mainPanel, ex.getMessage());
					}
				}
			}
			else
			{
				// 存在する場合
				pathTextArea.replaceRange("", selectionStart, selectionEnd);
			}
		});
		deleteItem.setFont(new Font("Yu Gothic UI", Font.PLAIN, 20));
		contextMenu.add(deleteItem);

		// セパレータ
		contextMenu.addSeparator();

		// 行選択
		JMenuItem selectRowItem = new JMenuItem("行選択");
		selectRowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
		selectRowItem.addActionListener(e -> selectLine());
		selectRowItem.setFont(new Font("Yu Gothic UI", Font.PLAIN, 20));
		contextMenu.add(selectRowItem);

		// 全選択
		JMenuItem selectAllItem = new JMenuItem("全選択");
		selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		selectAllItem.addActionListener(e -> pathTextArea.selectAll());
		selectAllItem.setFont(new Font("Yu Gothic UI", Font.PLAIN, 20));
		contextMenu.add(selectAllItem);

		// セパレータ
		contextMenu.addSeparator();

		// 選択行のフォルダを開く
		JMenuItem openFolderItem = new JMenuItem("選択行のフォルダを開く　");
		openFolderItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		openFolderItem.addActionListener(e ->
		{
			openFolderAction(mainPanel);
		});
		openFolderItem.setFont(new Font("Yu Gothic UI", Font.PLAIN, 20));
		contextMenu.add(openFolderItem);

		// コンテキストメニュー設定
		contextMenu.addPopupMenuListener(new PopupMenuListener()
		{
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e)
			{
				// 元に戻す・やり直すの活性状態を設定する。
				undoItem.setEnabled(undoManager.canUndo());
				redoItem.setEnabled(undoManager.canRedo());
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
			{
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e)
			{
			}
		});

		// コンテキストメニューを設定する。
		pathTextArea.setComponentPopupMenu(contextMenu);

		// パステキストエリアにフォーカスを設定する。
		pathTextArea.requestFocusInWindow();
	}

	/**
	 * テキストエリア・Tabキー遷移設定処理
	 * @param textArea テキストエリア
	 */
	private void setTabFocusTraversal(JTextArea textArea)
	{
		// Tabキーで次のコンポーネントにフォーカスを移動させる設定を行う。
		textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "focusNext");
		textArea.getActionMap().put("focusNext", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				textArea.transferFocus();
			}
		});

		// Shift+Tabキーで前のコンポーネントにフォーカスを移動させる設定を行う。
		textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK), "focusPrevious");
		textArea.getActionMap().put("focusPrevious", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				textArea.transferFocusBackward();
			}
		});
	}

	/**
	 * 行選択処理
	 */
	private void selectLine()
	{
		String text = pathTextArea.getText();

		if (text.isBlank())
		{
			return;
		}

		// 選択開始行位置・選択終了行位置を取得・保持する。
		int startLineIndex = getLineIndex(text, pathTextArea.getSelectionStart());
		int endLineIndex = getLineIndex(text, pathTextArea.getSelectionEnd());

		// 選択開始位置・選択終了位置を取得・保持する。
		int lineStartOffset = getLineStartOffset(text, startLineIndex);
		int lineEndOffset = getLineEndOffset(text, endLineIndex);

		// 選択範囲を設定する。
		pathTextArea.setSelectionStart(lineStartOffset);
		pathTextArea.setSelectionEnd(lineEndOffset);

		pathTextArea.requestFocus();
	}

	/**
	 * 行開始位置取得処理
	 * @param text テキスト
	 * @param lineIndex 行位置
	 * @return　行開始位置
	 */
	private int getLineStartOffset(String text, int lineIndex)
	{
		int lineCount = 0;

		// 全ての文字に処理を行う。
		for (int index = 0; index < text.length(); index++)
		{
			if (lineCount == lineIndex)
			{
				return index;
			}

			// 文字を判定する。
			if (text.charAt(index) == '\n')
			{
				// 改行コードの場合、行数をインクリメントする。
				lineCount++;
			}
		}

		// 行数が指定された行より少ない場合、全体文字数を返却する。（今回の場合、ここに到達する事はない。）
		return text.length();
	}

	/**
	 * 行終了位置取得処理
	 * @param text テキスト
	 * @param lineIndex 行位置
	 * @return　行終了位置
	 */
	private int getLineEndOffset(String text, int lineIndex)
	{
		int lineCount = 0;

		// 全ての文字に処理を行う。
		for (int index = 0; index < text.length(); index++)
		{
			// 行位置・文字を判定する。
			if (lineCount == lineIndex && text.charAt(index) == '\n')
			{
				// 対象の行位置、かつ改行コードの場合、位置を返却する。
				return index;
			}

			// 文字を判定する。
			if (text.charAt(index) == '\n')
			{
				// 改行コードの場合、行数をインクリメントする。
				lineCount++;
			}
		}

		// 改行コードが存在しない場合、全体文字数を返却する。
		return text.length();
	}

	/**
	 * 行位置取得処理
	 * @param text テキスト
	 * @param offset 位置
	 */
	private int getLineIndex(String text, int offset)
	{
		String[] lines = text.substring(0, offset).split("\n", -1);
		return lines.length - 1;
	}

	/**
	 * 選択行のフォルダを開くアクション
	 * @param mainPanel メインパネル
	 */
	private void openFolderAction(JPanel mainPanel)
	{
		try
		{
			if (pathTextArea.getText().isBlank())
			{
				return;
			}

			// 選択開始行位置・選択終了行位置を取得・保持する。
			int startLineIndex = pathTextArea.getLineOfOffset(pathTextArea.getSelectionStart());
			int endLineIndex = pathTextArea.getLineOfOffset(pathTextArea.getSelectionEnd());

			String path;

			// パステキストエリアの空行も含め、全行取得する。
			String[] pathAr = pathTextArea.getText().split("\n", -1);
			List<String> openFolderPathList = new ArrayList<>();

			// 表示フォルダリストを設定する。
			for (int index = startLineIndex; index <= endLineIndex; index++)
			{
				// パスを保持する。
				path = pathAr[index];

				// パスの有無を判定する。
				if (!path.isBlank())
				{
					// ドライブ文字を、大文字に変換し、保持する。
					path = path.substring(0, 1).toUpperCase() + path.substring(1);

					// パスの末尾を判定する。
					if ("\\".equals(path.substring(path.length() - 1)))
					{
						// 【\】の場合、削除する。
						path = path.substring(0, path.length() - 1);
					}

					// パスの有無を判定する。
					if (!openFolderPathList.contains(path))
					{
						// 未設定のパスの場合
						openFolderPathList.add(path);
					}
				}
			}

			// フォルダを開く。
			for (String openFolderPath : openFolderPathList)
			{
				File folder = new File(openFolderPath);
				if (folder.exists() && folder.isDirectory())
				{
					Desktop.getDesktop().open(folder);
				}
			}
		}
		catch (Exception ex)
		{
			utility.showCatchErrorMessage(mainPanel, ex.getMessage());
		}
	}

	/**
	 * ドロップターゲットリスナー
	 */
	private class FolderDropTargetListener extends DropTargetAdapter
	{
		/**
		 * コンポーネント
		 */
		private Component parent;

		/**
		 * コンストラクタ
		 * @param parentComponent 親コンポーネント
		 */
		public FolderDropTargetListener(Component parentComponent)
		{
			parent = parentComponent;
		}

		@Override
		public void dragEnter(DropTargetDragEvent event)
		{
			// コピーアクションを許可する。
			if (event.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			{
				event.acceptDrag(DnDConstants.ACTION_COPY);
			}
			else
			{
				event.rejectDrag();
			}
		}

		@Override
		public void dragOver(DropTargetDragEvent event)
		{
			// ドラッグ中もコピーアクションを許可する。
			if (event.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			{
				event.acceptDrag(DnDConstants.ACTION_COPY);
			}
			else
			{
				event.rejectDrag();
			}
		}

		@Override
		public void drop(DropTargetDropEvent event)
		{
			try
			{
				if (event.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
				{
					event.acceptDrop(DnDConstants.ACTION_COPY);

					Object transferData = event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					if (transferData instanceof List)
					{
						// 【型の安全性: Object から List<File> への未検査キャスト】というエラーが発生するので、一旦【List<?>に】キャストしている。
						List<?> dataList = (List<?>) transferData;
						List<File> droppedItemList = dataList.stream()
								.filter(File.class::isInstance)
								.map(File.class::cast)
								.toList();

						String pathText = pathTextArea.getText();

						// ドロップした全項目に対して処理を行う。
						for (File item : droppedItemList)
						{
							// 項目を判定する。
							if (item.isDirectory())
							{
								if (pathText.isEmpty())
								{
									pathText = item.getAbsolutePath();
								}
								else
								{
									pathText = pathText + "\n" + item.getAbsolutePath();
								}
							}
						}

						// テキストを設定する。
						pathTextArea.setText(pathText);

						// パステキストエリアにフォーカスを設定し、カーソルを末尾に設定する。
						pathTextArea.requestFocusInWindow();
						pathTextArea.setCaretPosition(pathText.length());
					}

					event.dropComplete(true);
				}
				else
				{
					event.rejectDrop();
				}
			}
			catch (Exception e)
			{
				utility.showCatchErrorMessage(parent, e.getMessage());
				event.dropComplete(false);
			}
		}
	}
}
