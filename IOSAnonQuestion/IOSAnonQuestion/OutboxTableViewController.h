//
//  MessagesTableViewController.h
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <CoreData/CoreData.h>

#import "AskQuestionNavigationController.h"

#import "CurrencyBarButtonItem.h"

#import "QuestionTableViewCell.h"

#import "Question.h"

@interface OutboxTableViewController : UITableViewController<NSFetchedResultsControllerDelegate>

@property (nonatomic, strong) NSManagedObjectContext *managedObjectContext;

@end
