//
//  AskQuestionTableViewController.m
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import "AskQuestionTableViewController.h"
#import "Question.h"
#import "Cloud.h"
#import "Settings.h"

@interface AskQuestionTableViewController ()

@end

@implementation AskQuestionTableViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        self.navigationItem.title = @"Ask";
        self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(send:)];
        self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(cancel:)];
        
        self.selectedIndex = [self indexForRecipientsCount:[[Settings sharedInstance] defaultRecipientsCount] ];
        
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
#warning Incomplete method implementation.
    // Return the number of rows in the section.
    if(section == 0){
        return 1;
    }else{
        return 3;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if(!cell){
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:CellIdentifier];
    }else{
        cell.detailTextLabel.text = @"";
    }
    
    if(indexPath.section == 0){ //
        if(indexPath.row == 0){ // Question Text
            if(self.questionTextField == nil){
                self.questionTextField = [[LimitedTextView alloc] initWithFrame:CGRectMake(0, 0, 300, 80)];
                [self.questionTextField.textView becomeFirstResponder];
                
            }
            [cell.contentView addSubview:self.questionTextField];
            
        }
    
    }else if(indexPath.section == 1){
        if(indexPath.row == 0){ // Number of recipients
            // 1
            cell.textLabel.text = @"1";
        }else if(indexPath.row == 1){
            // 5
            cell.textLabel.text = @"5";
        }else if(indexPath.row == 2){
            // 10
            cell.textLabel.text = @"10";
        }
        if(indexPath.row == self.selectedIndex){
            cell.accessoryType = UITableViewCellAccessoryCheckmark;
        }else{
            cell.accessoryType = UITableViewCellAccessoryNone;
        }
        if([self indexForRecipientsCount:[[Settings sharedInstance] defaultRecipientsCount] ] == indexPath.row){
            cell.detailTextLabel.text= @"Default";
        }else{
            cell.detailTextLabel.text= @"";
        }
    }
    
    return cell;
}


-(NSString*)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section{
    if(section == 0){
        NSString * questionTitle = NSLocalizedStringWithDefaultValue(@"QuestionTableTitle", @"", [NSBundle mainBundle], @"Question", @"Text displayed to the user right above where they type in their question");
        return questionTitle;
    }else if(section == 1){
        NSString * recipientCount = NSLocalizedStringWithDefaultValue(@"QuestionTableRecipients", @"", [NSBundle mainBundle], @"Number of Recipients", @"Displayed to the user right above where they select the number of recipients.");
        return recipientCount;
    }
    return nil;
}



-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.section == 0){
        return 80;
    }else if(indexPath.section == 1){
        return 30;
    }
    return 0;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.section == 1){
        self.selectedIndex = indexPath.row;
        NSIndexSet * set = [[NSIndexSet alloc] initWithIndex:1];
        [tableView reloadSections:set withRowAnimation:UITableViewRowAnimationFade];
    }
}


#pragma mark Event Handlers

-(void)send:(id)sender{
    NSString * questionText = @"testing";
    Question * question = [[Question alloc] initWithText:questionText];
    [[Cloud sharedInstance] askQuestion:question delegate:self];
}

-(void)cancel:(id)sender{
    // TODO: fix this hack with delegates
    [self.navigationController dismissViewControllerAnimated:YES completion:^(void){
        
    }];
    
}

#pragma mark AskQuestionDelegate methods

-(void)questionAskDidSucceed{
    // TODO: Add it to the internal model (Core Data)
    
    
    [self.navigationController dismissViewControllerAnimated:YES completion:^(void){
        
    }];
}

-(void)questionAskDidFail{
    
    NSString * failTitle = NSLocalizedStringWithDefaultValue(@"QuestionAskFailTitle", @"", [NSBundle mainBundle], @"Error", @"Title of the alert dialog that is shown to the user when they try and ask a question and it fails for some reason.");
    
    NSString * failMessage = NSLocalizedStringWithDefaultValue(@"QuestionAskFailMessage", @"", [NSBundle mainBundle], @"Please ensure you have an active internet connection and try again", @"Message of the alert dialog that is shown to the user when they try and ask a question and it fails for some reason.");
    
    // Display an error message and try again
    UIAlertView * alert = [[UIAlertView alloc] initWithTitle:failTitle message:failMessage delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
    
    [alert show];
}

#pragma mark Convienence mapping methods

-(NSInteger)indexForRecipientsCount:(NSInteger)recipientsCount{
    if(recipientsCount == 1){
        return 0;
    }else if(recipientsCount == 5){
        return 1;
    }else if(recipientsCount == 10){
        return 2;
    }
    return 0; // WTF
}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

/*
#pragma mark - Navigation

// In a story board-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}

 */

@end
