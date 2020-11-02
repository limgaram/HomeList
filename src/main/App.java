package main;

import java.util.ArrayList;
import java.util.Scanner;

public class App {
	ArticleDao articleDao = new ArticleDao();
	ReplyDao replyDao = new ReplyDao();
	MemberDao memberDao = new MemberDao();
	
	Member loginedMember = null;
	
	public void start() {
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			if(loginedMember == null) {
				System.out.println("��ɾ� �Է� : ");
			} else {
				System.out.println("���� �Է�[" + loginedMember.getLoginId() + "(" + loginedMember.getNickname() + ") ] : ");
				
			}
			
			String cmd = sc.nextLine();
			
			if(cmd.contentEquals("exit")) {
				System.out.println("����");
				break;
			}
			
			if(cmd.contentEquals("help")) {
				System.out.println("article [add: �Խù� �߰� / list : �Խù� ��� ��ȸ / read : �Խù� ��ȸ / search : �˻�]");
				System.out.println("member [signup : ȸ������ / signin : �α��� / findpass : ��й�ȣ ã�� / findid : ���̵� ã�� / logout : �α׾ƿ� / myinfo : ���� ���� Ȯ�ι� ����]");
			}
			
			if(cmd.contentEquals("article add")) {
				
				if(!isLogin()) {
					continue;
				}
				
				Article a = new Article();
				
				System.out.println("�Խù� ������ �Է����ּ��� . : ");
				String title = sc.nextLine();
				a.setTitle(title);
				
				System.out.println("�Խù� ������ �Է����ּ���. : ");
				String body = sc.nextLine();
				a.setBody(body);
				
				a.setNickname(loginedMember.getNickname());
				
				articleDao.insertArticle(a);
				System.out.println("�Խù��� ��ϵǾ����ϴ�.");
			}
			
			if(cmd.contentEquals("article update")) {
				
				System.out.println("������ �Խù� ���� : ");
				int targetId = Integer.parseInt(sc.nextLine());
				
				Article target = articleDao.getArticleById(targetId);
				if(target == null) {
					System.out.println("���� �Խù��Դϴ�.");
				} else {
					
					System.out.println("�Խù� ������ �Է����ּ���. : ");
					String newTitle = sc.nextLine();
					target.setTitle(newTitle);
					
					System.out.println("�Խù� ������ �Է����ּ���. : ");
					String newBody = sc.nextLine();
					target.setBody(newBody);
					
					break;
				}
				
				if(cmd.equals("article delete")) {
					ArrayList<Article> articles = articleDao.getArticles();
					System.out.println("������ �Խù� ���� : ");
					int targetId = Integer.parseInt(sc.nextLine());
					Article target = articleDao.getArticleById(targetId);
					
					if(target == null) {
						System.out.println("�Խù��� �������� �ʽ��ϴ�.");
					} else {
						articleDao.removeArticle(target);
					}
					
				}
				
				if (cmd.equals("article read")) {
					System.out.println("�󼼺����� �Խù� ���� : ");
					int targetId = Integer.parseInt(sc.nextLine());
					Article target = articleDao.getArticleById(targetId);
					if (target == null) {
						System.out.println("�Խù��� �������� �ʽ��ϴ�.");
					} else {
						target.setHit(target.getHit() + 1);
						printArticle(target);

						while (true) {
							System.out.println("�󼼺��� ����� �������ּ���(1. ��� ���, 2. ���ƿ�, 3. ����, 4. ����, 5. �������) :");
							int readCmd = Integer.parseInt(sc.nextLine());
							if (readCmd == 1) {
								Reply r = new Reply();

								System.out.println("��� ������ �Է����ּ���:");
								String body = sc.nextLine();
								r.setParentId(target.getId());
								r.setBody(body);
								r.setNickname("�͸�");

								replyDao.insertReply(r);
								System.out.println("����� ��ϵǾ����ϴ�.");
								printArticle(target);

							} else if (readCmd == 2) {
								System.out.println("���ƿ� ���");
							} else if (readCmd == 3) {
								System.out.println("���� ���");
							} else if (readCmd == 4) {
								System.out.println("���� ���");
							} else if (readCmd == 5) {
								break;
							}
						}
					}
				}
				if (cmd.equals("article search")) {
					System.out.println("�˻� �׸��� (1. ����, 2. ����, 3. ���� + ����, 4. �ۼ���) : ");
					int flag = Integer.parseInt(sc.nextLine());
					System.out.println("�˻� Ű���带 �Է����ּ��� : ");
					String keyword = sc.nextLine();
					ArrayList<Article> searchedArticles;

					searchedArticles = articleDao.getSearchedArticlesByFlag(flag, keyword);

					printArticles(searchedArticles);
				}
				if (cmd.equals("member signup")) {
					System.out.println("======== ȸ�������� �����մϴ�.========");
					Member m = new Member();

					System.out.println("���̵� �Է����ּ��� :");
					String id = sc.nextLine();
					m.setLoginId(id);

					System.out.println("��й�ȣ�� �Է����ּ��� :");
					String pw = sc.nextLine();
					m.setLoginPw(pw);

					System.out.println("�г����� �Է����ּ��� :");
					String nick = sc.nextLine();
					m.setNickname(nick);

					memberDao.insertMember(m);
					System.out.println("======== ȸ�������� �Ϸ�Ǿ����ϴ�.========");
				}
				if (cmd.equals("member signin")) {
					System.out.println("���̵� :");
					String id = sc.nextLine();

					System.out.println("��й�ȣ :");
					String pw = sc.nextLine();

					Member member = memberDao.getMemberByLoginIdAndLoginPw(id, pw);
					if (member == null) {
						System.out.println("��й�ȣ�� Ʋ�Ȱų� �߸��� ȸ�������Դϴ�.");
					} else {
						loginedMember = member;
						System.out.println(loginedMember.getNickname() + "�� �ȳ��ϼ���!!");
					}

				}
				if (cmd.equals("member logout")) {

					if (!isLogin()) {
						continue;
					}

					loginedMember = null;
					System.out.println("�α׾ƿ� �Ǽ̽��ϴ�.");

				}
			}
		}

		private void printArticles(ArrayList<Article> articleList) {
			for (int i = 0; i < articleList.size(); i++) {
				Article article = articleList.get(i);
				System.out.println("��ȣ : " + article.getId());
				System.out.println("���� : " + article.getTitle());
				System.out.println("��ϳ�¥ : " + article.getRegDate());
				System.out.println("�ۼ��� : " + article.getNickname());
				System.out.println("��ȸ�� : " + article.getHit());
				System.out.println("===================");
			}
		}

		private void printReplies(ArrayList<Reply> replyList) {
			for (int i = 0; i < replyList.size(); i++) {
				Reply reply = replyList.get(i);
				System.out.println("���� : " + reply.getBody());
				System.out.println("�ۼ��� : " + reply.getNickname());
				System.out.println("��ϳ�¥ : " + reply.getRegDate());
				System.out.println("===================");
			}
		}

		private void printArticle(Article target) {
			System.out.println("==== " + target.getId() + " ====");
			System.out.println("��ȣ : " + target.getId());
			System.out.println("���� : " + target.getTitle());
			System.out.println("���� : " + target.getBody());
			System.out.println("��ϳ�¥ : " + target.getRegDate());
			System.out.println("��ȸ�� : " + target.getHit());
			System.out.println("===============");
			System.out.println("================���==============");

			ArrayList<Reply> replies = replyDao.getRepliesByParentId(target.getId());
			printReplies(replies);
		}

		private boolean isLogin() {
			if (loginedMember == null) {
				System.out.println("�α����� �ʿ��� ����Դϴ�.");
				return false;
			} else {
				return true;
			}
		}
	}