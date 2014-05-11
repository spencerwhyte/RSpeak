//
//  AskQuestionTableViewController.h
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "AskQuestionDelegate.h"
#import "LimitedTextView.h"

@interface AskQuestionTableViewController : UITableViewController <AskQuestionDelegate>

@property LimitedTextView * questionTextField;

-(void)questionAskDidFail;
-(void)questionAskDidSucceed;

@end
